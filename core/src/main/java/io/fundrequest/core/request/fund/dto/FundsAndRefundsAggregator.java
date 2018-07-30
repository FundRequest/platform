package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.request.fund.UserFundsDto;
import io.fundrequest.core.token.dto.TokenValueDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.math.BigDecimal.ZERO;

@Component
public class FundsAndRefundsAggregator {

    public List<UserFundsDto> aggregate(final List<FundsByFunderDto> fundsByFunder) {

        return fundsByFunder.stream()
                            .collect(Collectors.groupingBy(funds -> funds.getFunderAddress().toLowerCase() + funds.getFunderUserId(),
                                                           Collectors.mapping(mapToUserFundDto(), Collectors.reducing(mergeFundsAndRefunds()))))
                            .values()
                            .stream()
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList());
    }

    private BinaryOperator<UserFundsDto> mergeFundsAndRefunds() {
        return (userFundsDto1, userFundsDto2) -> UserFundsDto.builder()
                                                             .funderUserId(userFundsDto1.getFunderUserId())
                                                             .funderAddress(userFundsDto1.getFunderAddress())
                                                             .fndFunds(Optional.ofNullable(userFundsDto1.getFndFunds()).orElse(userFundsDto2.getFndFunds()))
                                                             .otherFunds(Optional.ofNullable(userFundsDto1.getOtherFunds()).orElse(userFundsDto2.getOtherFunds()))
                                                             .fndRefunds(Optional.ofNullable(userFundsDto1.getFndRefunds()).orElse(userFundsDto2.getFndRefunds()))
                                                             .otherRefunds(Optional.ofNullable(userFundsDto1.getOtherRefunds()).orElse(userFundsDto2.getOtherRefunds()))
                                                             .build();
    }

    private Function<FundsByFunderDto, UserFundsDto> mapToUserFundDto() {
        return fundsByFunder -> UserFundsDto.builder()
                                            .funderUserId(fundsByFunder.getFunderUserId())
                                            .funderAddress(fundsByFunder.getFunderAddress())
                                            .fndFunds(pickIfFund(fundsByFunder.getFndValue()))
                                            .otherFunds(pickIfFund(fundsByFunder.getOtherValue()))
                                            .fndRefunds(pickIfRefund(fundsByFunder.getFndValue()))
                                            .otherRefunds(pickIfRefund(fundsByFunder.getOtherValue()))
                                            .build();
    }

    private TokenValueDto pickIfFund(final TokenValueDto tokenValue) {
        return isFund(tokenValue) ? tokenValue : null;
    }

    private TokenValueDto pickIfRefund(final TokenValueDto tokenValue) {
        return !isFund(tokenValue) ? tokenValue : null;
    }

    private boolean isFund(final TokenValueDto tokenValue) {
        return tokenValue != null && tokenValue.getTotalAmount() != null && tokenValue.getTotalAmount().compareTo(ZERO) >= 0;
    }
}
