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
        return (fundsByFunder1, fundsByFunder2) -> FundsByFunderDto.builder()
                                                                  .funderUserId(fundsByFunder1.getFunderUserId())
                                                                  .funderAddress(fundsByFunder1.getFunderAddress())
                                                                  .fndValue(TokenValueDto.sum(fundsByFunder1.getFndValue(), fundsByFunder2.getFndValue()))
                                                                  .otherValue(TokenValueDto.sum(fundsByFunder1.getOtherValue(), fundsByFunder2.getOtherValue()))
                                                                  .build();
    }
}
