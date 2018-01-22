package io.fundrequest.core.request.claim.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class ClaimMother {

    private ClaimMother() {
    }

    public static ClaimBuilder aClaim() {
        return ClaimBuilder.aClaim()
                .withAmountInWei(
                        new BigDecimal("50330000000000000000")
                )
                .withSolver("davyvanroy")
                .withTimestamp(LocalDateTime.now())
                .withRequestId(1L);
    }
}