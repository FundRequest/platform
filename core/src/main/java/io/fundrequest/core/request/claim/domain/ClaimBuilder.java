package io.fundrequest.core.request.claim.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class ClaimBuilder {
    private Long id;
    private String solver;
    private BigDecimal amountInWei;
    private LocalDateTime timestamp;
    private Long requestId;

    private ClaimBuilder() {
    }

    public static ClaimBuilder aClaim() {
        return new ClaimBuilder();
    }

    public ClaimBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public ClaimBuilder withSolver(String solver) {
        this.solver = solver;
        return this;
    }

    public ClaimBuilder withAmountInWei(BigDecimal amountInWei) {
        this.amountInWei = amountInWei;
        return this;
    }

    public ClaimBuilder withTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public ClaimBuilder withRequestId(Long requestId) {
        this.requestId = requestId;
        return this;
    }

    public Claim build() {
        Claim claim = new Claim(solver, amountInWei, requestId, timestamp);
        claim.setId(id);
        return claim;
    }
}
