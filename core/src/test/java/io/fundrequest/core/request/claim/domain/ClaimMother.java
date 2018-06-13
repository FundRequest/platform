package io.fundrequest.core.request.claim.domain;

import io.fundrequest.core.token.model.TokenValue;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class ClaimMother {

    private ClaimMother() {
    }

    public static ClaimBuilder aClaim() {
        return ClaimBuilder.aClaim()
                           .withTokenValue(TokenValue.builder()
                                                     .tokenAddress("0x02f96ef85cad6639500ca1cc8356f0b5ca5bf1d2")
                                                     .amountInWei(new BigDecimal("50330000000000000000"))
                                                     .build())
                           .withSolver("davyvanroy")
                           .withTimestamp(LocalDateTime.now())
                           .withRequestId(1L);
    }
}