package io.fundrequest.core.request.domain;

import io.fundrequest.core.request.fund.domain.FundBuilder;

import java.math.BigDecimal;

public final class FundMother {

    public static FundBuilder aFund() {
        return FundBuilder
                .aFund()
                .withAmountInWei(
                        new BigDecimal("50330000000000000000")
                )
                .withFunder("davy")
                .withRequestId(1L);
    }
}
