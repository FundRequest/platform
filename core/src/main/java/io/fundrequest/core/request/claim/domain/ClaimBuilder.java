package io.fundrequest.core.request.claim.domain;

import io.fundrequest.core.token.model.TokenValue;

import java.time.LocalDateTime;

public final class ClaimBuilder {
    private Long id;
    private String solver;
    private TokenValue tokenValue;
    private String tokenHash;
    private LocalDateTime timestamp;
    private Long requestId;
    private Long blockchainEventId;

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

    public ClaimBuilder withTokenValue(TokenValue tokenValue) {
        this.tokenValue = tokenValue;
        return this;
    }

    public ClaimBuilder withTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
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

    public ClaimBuilder withBlockchainEventId(Long blockchainEventId) {
        this.blockchainEventId = blockchainEventId;
        return this;
    }

    public Claim build() {
        Claim claim = new Claim(solver, tokenValue, requestId, timestamp, blockchainEventId);
        claim.setId(id);
        return claim;
    }
}
