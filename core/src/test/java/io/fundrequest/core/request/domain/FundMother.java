package io.fundrequest.core.request.domain;

import io.fundrequest.core.request.fund.domain.Fund;

import java.math.BigDecimal;

public final class FundMother {

    public static Fund.FundBuilder aFund() {
        return Fund.builder()
                .amountInWei(
                        new BigDecimal("50330000000000000000")
                )
                .funder("davy")
                .requestId(1L)
                .token("0xe5dada80aa6477e85d09747f2842f7993d0df71c");
    }
}
