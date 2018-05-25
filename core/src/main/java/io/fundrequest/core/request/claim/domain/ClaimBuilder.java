package io.fundrequest.core.request.claim.domain;

import io.fundrequest.core.request.domain.BlockchainEvent;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class ClaimBuilder {
    private Long id;
    private String solver;
    private BigDecimal amountInWei;
    private LocalDateTime timestamp;
    private String tokenHash;
    private Long requestId;
    private BlockchainEvent blockchainEvent;

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

    public ClaimBuilder withTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
        return this;
    }

    public ClaimBuilder withRequestId(Long requestId) {
        this.requestId = requestId;
        return this;
    }

    public ClaimBuilder withBlockchainEvent(BlockchainEvent blockchainEvent) {
        this.blockchainEvent = blockchainEvent;
        return this;
    }

    public Claim build() {
        Claim claim = new Claim(solver, amountInWei, tokenHash, requestId, timestamp, blockchainEvent);
        claim.setId(id);
        return claim;
    }
}
