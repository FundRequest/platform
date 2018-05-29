package io.fundrequest.core.request.claim;

import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.claim.dto.ClaimsAggregate;
import io.fundrequest.core.request.fiat.FiatService;
import io.fundrequest.core.token.dto.TokenValueDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class ClaimDtoAggregator {

    private final String fndContractAdress;
    private final FiatService fiatService;

    public ClaimDtoAggregator(@Value("${io.fundrequest.contract.token.address}") final String fndContractAdress,
                              final FiatService fiatService) {
        this.fndContractAdress = fndContractAdress;
        this.fiatService = fiatService;
    }

    ClaimsAggregate aggregateClaims(final List<ClaimDto> claims) {
        final TokenValueDto fndValue = claims.stream()
                                             .filter(this::isFNDToken)
                                             .map(ClaimDto::getTokenValue)
                                             .reduce(this::sumTokenValue)
                                             .orElse(null);
        final TokenValueDto otherValue = claims.stream()
                                               .filter(claim -> !isFNDToken(claim))
                                               .map(ClaimDto::getTokenValue)
                                               .reduce(this::sumTokenValue)
                                               .orElse(null);
        return ClaimsAggregate.builder()
                              .claims(claims)
                              .fndValue(fndValue)
                              .otherValue(otherValue)
                              .usdValue(fiatService.getUsdPrice(fndValue, otherValue))
                              .build();
    }

    private boolean isFNDToken(final ClaimDto claim) {
        return fndContractAdress.equalsIgnoreCase(claim.getTokenValue().getTokenAddress());
    }

    private TokenValueDto sumTokenValue(final TokenValueDto tokenValue, final TokenValueDto tokenValue2) {
        return TokenValueDto.builder()
                            .tokenAddress(tokenValue.getTokenAddress())
                            .tokenSymbol(tokenValue.getTokenSymbol())
                            .totalAmount(tokenValue.getTotalAmount().add(tokenValue2.getTotalAmount()))
                            .build();
    }
}
