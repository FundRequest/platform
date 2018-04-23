package io.fundrequest.core.request.domain;

import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.token.dto.TokenInfoDtoMother;

import java.math.BigDecimal;

public final class FundMother {

    public static Fund.FundBuilder fndFundFunderKnown() {
        return Fund.builder()
                   .amountInWei(
                           new BigDecimal("3870000000000000000")
                               )
                   .funderUserId("e7356d6a-4eff-4003-8736-557c36ce6e0c")
                   .funder("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853")
                   .requestId(1L)
                   .token(TokenInfoDtoMother.fnd().getAddress());
    }

    public static Fund.FundBuilder fndFundFunderNotKnown() {
        return Fund.builder()
                   .amountInWei(
                           new BigDecimal("4870000000000000000")
                               )
                   .funder("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853")
                   .requestId(1L)
                   .token(TokenInfoDtoMother.fnd().getAddress());
    }

    public static Fund.FundBuilder zrxFundFunderKnown() {
        return Fund.builder()
                   .amountInWei(
                           new BigDecimal("2767000000000000000")
                               )
                   .funderUserId("e7356d6a-4eff-4003-8736-557c36ce6e0c")
                   .funder("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853")
                   .requestId(1L)
                   .token(TokenInfoDtoMother.zrx().getAddress());
    }

    public static Fund.FundBuilder zrxFundFunderNotKnown() {
        return Fund.builder()
                   .amountInWei(
                           new BigDecimal("1767000000000000000")
                               )
                   .funder("0xd24400ae8BfEBb18cA49Be86258a3C749cf46853")
                   .requestId(1L)
                   .token(TokenInfoDtoMother.zrx().getAddress());
    }
}
