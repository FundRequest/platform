package io.fundrequest.core.request.view;

import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.dto.FundDtoMapperImpl;
import io.fundrequest.core.token.dto.TokenInfoDtoMother;
import io.fundrequest.core.token.dto.TokenValueDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

public final class FundDtoMother {

    private final static FundDtoMapperImpl mapper;

    static {
        mapper = new FundDtoMapperImpl();
    }

    public static FundDto.FundDtoBuilder aFundDto() {
        return FundDto.builder()
                      .tokenValue(TokenValueDto.builder()
                                               .totalAmount(new BigDecimal("3.870000000000000000"))
                                               .tokenAddress(TokenInfoDtoMother.fnd().getAddress())
                                               .build())
                      .blockchainEventId(3465L)
                      .funderAddress("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853")
                      .requestId(1L)
                      .timestamp(LocalDateTime.of(2017, Month.DECEMBER, 27, 0, 0));
    }
}
