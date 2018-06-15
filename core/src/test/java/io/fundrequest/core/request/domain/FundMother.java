package io.fundrequest.core.request.domain;

import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.token.dto.TokenInfoDtoMother;
import io.fundrequest.core.token.model.TokenValue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class FundMother {

    public static Fund.FundBuilder fndFundFunderKnown() {
        return Fund.builder()
                   .tokenValue(TokenValue.builder()
                                         .amountInWei(new BigDecimal("3870000000000000000"))
                                         .tokenAddress(TokenInfoDtoMother.fnd().getAddress())
                                         .build())
                   .funderUserId("e7356d6a-4eff-4003-8736-557c36ce6e0c")
                   .funderAddress("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853")
                   .timestamp(LocalDateTime.now())
                   .requestId(1L);
    }

    public static Fund.FundBuilder fndFundFunderNotKnown() {
        return Fund.builder()
                   .tokenValue(TokenValue.builder()
                                         .amountInWei(new BigDecimal("4870000000000000000"))
                                         .tokenAddress(TokenInfoDtoMother.fnd().getAddress())
                                         .build())
                   .funderAddress("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853")
                   .timestamp(LocalDateTime.now())
                   .requestId(1L);
    }

    public static Fund.FundBuilder zrxFundFunderKnown() {
        return Fund.builder()
                   .tokenValue(TokenValue.builder()
                                         .amountInWei(new BigDecimal("38700000276700000000000000000000000000"))
                                         .tokenAddress(TokenInfoDtoMother.zrx().getAddress())
                                         .build())
                   .funderUserId("e7356d6a-4eff-4003-8736-557c36ce6e0c")
                   .funderAddress("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853")
                   .timestamp(LocalDateTime.now())
                   .requestId(1L);
    }

    public static Fund.FundBuilder zrxFundFunderNotKnown() {
        return Fund.builder()
                   .tokenValue(TokenValue.builder()
                                         .amountInWei(new BigDecimal("1767000000000000000"))
                                         .tokenAddress(TokenInfoDtoMother.zrx().getAddress())
                                         .build())
                   .funderAddress("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853")
                   .timestamp(LocalDateTime.now())
                   .requestId(1L);
    }
}
