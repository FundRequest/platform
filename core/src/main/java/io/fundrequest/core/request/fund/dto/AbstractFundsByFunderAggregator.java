package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.token.dto.TokenValueDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public abstract class AbstractFundsByFunderAggregator<T> {

    public List<FundsByFunderDto> aggregate(final List<T> funds) {
        return funds.stream()
                    .collect(Collectors.groupingBy(fund -> getFunderAddress(fund) + getFundedBy(fund),
                                                   Collectors.mapping(mapToFundsByFunderDto(), Collectors.reducing(sumFunds()))))
                    .values()
                    .stream()
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());
    }

    protected abstract String getFundedBy(T refund);

    protected abstract String getFunderAddress(T refund);

    protected abstract Function<T, FundsByFunderDto> mapToFundsByFunderDto();

    private BinaryOperator<FundsByFunderDto> sumFunds() {
        return (fundsByFunder, fundsByFunder2) -> FundsByFunderDto.builder()
                                                                  .funderUserId(fundsByFunder.getFunderUserId())
                                                                  .funderAddress(fundsByFunder.getFunderAddress())
                                                                  .fndValue(sumTokenValue(fundsByFunder.getFndValue(), fundsByFunder2.getFndValue()))
                                                                  .otherValue(sumTokenValue(fundsByFunder.getOtherValue(), fundsByFunder2.getOtherValue()))
                                                                  .build();
    }

    private TokenValueDto sumTokenValue(final TokenValueDto value1, final TokenValueDto value2) {
        if (value1 == null && value2 == null) {
            return null;
        }
        if (value1 == null) {
            return value2;
        }
        if (value2 == null) {
            return value1;
        }
        return TokenValueDto.builder()
                            .tokenAddress(value1.getTokenAddress())
                            .tokenSymbol(value1.getTokenSymbol())
                            .totalAmount(value1.getTotalAmount().add(value2.getTotalAmount()))
                            .build();
    }
}
