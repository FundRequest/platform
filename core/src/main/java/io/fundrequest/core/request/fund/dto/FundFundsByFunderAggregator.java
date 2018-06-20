package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.token.mapper.TokenValueDtoMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class FundFundsByFunderAggregator extends AbstractFundsByFunderAggregator<Fund> {

    private final String fndTokenHash;
    private final TokenValueDtoMapper tokenValueDtoMapper;

    public FundFundsByFunderAggregator(@Value("${io.fundrequest.contract.token.address}") final String fndTokenHash, final TokenValueDtoMapper tokenValueDtoMapper) {
        this.fndTokenHash = fndTokenHash;
        this.tokenValueDtoMapper = tokenValueDtoMapper;
    }

    @Override
    protected String getFundedBy(Fund fund) {
        return fund.getFunderAddress();
    }

    @Override
    protected String getFunderAddress(Fund fund) {
        return fund.getFunderUserId();
    }

    @Override
    protected Function<Fund, FundsByFunderDto> mapToFundsByFunderDto() {
        return fund -> FundsByFunderDto.builder()
                                       .funderAddress(fund.getFunderAddress())
                                       .funderUserId(fund.getFunderUserId())
                                       .fndValue(isFndToken(fund) ? tokenValueDtoMapper.map(fund.getTokenValue()) : null)
                                       .otherValue(!isFndToken(fund) ? tokenValueDtoMapper.map(fund.getTokenValue()) : null)
                                       .build();
    }

    private boolean isFndToken(final Fund templateFund) {
        return fndTokenHash.equalsIgnoreCase(templateFund.getTokenValue().getTokenAddress());
    }
}
