package io.fundrequest.core.request.claim;

import io.fundrequest.core.request.claim.dto.ClaimByTransactionAggregate;
import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.claim.dto.ClaimsByTransactionAggregate;
import io.fundrequest.core.request.fiat.FiatService;
import io.fundrequest.core.token.dto.TokenValueDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Component
class ClaimDtoAggregator {

    private final String fndContractAdress;
    private final FiatService fiatService;

    public ClaimDtoAggregator(@Value("${io.fundrequest.contract.token.address}") final String fndContractAdress,
                              final FiatService fiatService) {
        this.fndContractAdress = fndContractAdress;
        this.fiatService = fiatService;
    }

    ClaimsByTransactionAggregate aggregateClaims(final List<ClaimDto> claims) {
        TokenValueDto totalFndValue = null;
        TokenValueDto totalOtherValue = null;
        final Map<String, ClaimByTransactionAggregate.Builder> claimsPerTransaction = new HashMap<>();
        for (final ClaimDto claim : claims) {
            final ClaimByTransactionAggregate.Builder builder = resolveClaimByTransactionBuilder(claimsPerTransaction, claim);
            if (isFNDToken(claim)) {
                totalFndValue = sumTokenValue(totalFndValue, claim.getTokenValue());
                builder.fndValue(claim.getTokenValue());
            } else {
                totalOtherValue = sumTokenValue(totalOtherValue, claim.getTokenValue());
                builder.otherValue(claim.getTokenValue());
            }
            claimsPerTransaction.put(claim.getTransactionHash(), builder);
        }

        return ClaimsByTransactionAggregate.builder()
                                           .claims(Collections.unmodifiableList(claimsPerTransaction.keySet()
                                                                                                    .stream()
                                                                                                    .map(transactionHash -> claimsPerTransaction.get(transactionHash).build())
                                                                                                    .collect(toList())))
                                           .fndValue(totalFndValue)
                                           .otherValue(totalOtherValue)
                                           .usdValue(fiatService.getUsdPrice(totalFndValue, totalOtherValue))
                                           .build();
    }

    private ClaimByTransactionAggregate.Builder resolveClaimByTransactionBuilder(final Map<String, ClaimByTransactionAggregate.Builder> claimsPerTransaction,
                                                                                 final ClaimDto claim) {
        if (claimsPerTransaction.containsKey(claim.getTransactionHash())) {
            return claimsPerTransaction.get(claim.getTransactionHash());
        } else {
            return ClaimByTransactionAggregate.builder()
                                              .solver(claim.getSolver())
                                              .timestamp(claim.getTimestamp())
                                              .transactionHash(claim.getTransactionHash());
        }
    }

    private boolean isFNDToken(final ClaimDto claim) {
        return fndContractAdress.equalsIgnoreCase(claim.getTokenValue().getTokenAddress());
    }

    private TokenValueDto sumTokenValue(final TokenValueDto tokenValue, final TokenValueDto tokenValue2) {
        if (tokenValue == null) {
            return tokenValue2;
        }
        if (tokenValue2 == null) {
            return tokenValue;
        }
        return TokenValueDto.builder()
                            .tokenAddress(tokenValue.getTokenAddress())
                            .tokenSymbol(tokenValue.getTokenSymbol())
                            .totalAmount(tokenValue.getTotalAmount().add(tokenValue2.getTotalAmount()))
                            .build();
    }
}
