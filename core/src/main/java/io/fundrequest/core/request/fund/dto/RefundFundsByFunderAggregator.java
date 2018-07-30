package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.request.fund.domain.Refund;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.mapper.TokenValueDtoMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class RefundFundsByFunderAggregator extends AbstractFundsByFunderAggregator<Refund> {

    private final String fndTokenHash;
    private final TokenValueDtoMapper tokenValueDtoMapper;

    public RefundFundsByFunderAggregator(@Value("${io.fundrequest.contract.token.address}") final String fndTokenHash, final TokenValueDtoMapper tokenValueDtoMapper) {
        this.fndTokenHash = fndTokenHash;
        this.tokenValueDtoMapper = tokenValueDtoMapper;
    }

    @Override
    protected String getFundedBy(final Refund refund) {
        return refund.getRequestedBy();
    }

    @Override
    protected String getFunderAddress(final Refund refund) {
        return refund.getFunderAddress();
    }

    @Override
    protected Function<Refund, FundsByFunderDto> mapToFundsByFunderDto() {
        return refund -> FundsByFunderDto.builder()
                                         .funderAddress(refund.getFunderAddress())
                                         .funderUserId(refund.getRequestedBy())
                                         .fndValue(isFndToken(refund) ? negate(tokenValueDtoMapper.map(refund.getTokenValue())) : null)
                                         .otherValue(!isFndToken(refund) ? negate(tokenValueDtoMapper.map(refund.getTokenValue())) : null)
                                         .build();
    }

    private TokenValueDto negate(final TokenValueDto tokenValueDto) {
        return TokenValueDto.builder()
                            .tokenSymbol(tokenValueDto.getTokenSymbol())
                            .tokenAddress(tokenValueDto.getTokenAddress())
                            .totalAmount(tokenValueDto.getTotalAmount().negate())
                            .build();
    }

    private boolean isFndToken(final Refund templateRefund) {
        return fndTokenHash.equalsIgnoreCase(templateRefund.getTokenValue().getTokenAddress());
    }
}
