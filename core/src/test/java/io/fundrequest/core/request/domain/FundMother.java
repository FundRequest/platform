package io.fundrequest.core.request.domain;

import io.fundrequest.core.request.fund.domain.FundBuilder;

import java.math.BigInteger;

public final class FundMother {

    public static FundBuilder aFund() {
        return FundBuilder
                .aFund()
                .withAmountInWei(
                        new BigInteger("1000")
                )
                .withFunder("davy")
                .withRequestId(1L);
    }
}
