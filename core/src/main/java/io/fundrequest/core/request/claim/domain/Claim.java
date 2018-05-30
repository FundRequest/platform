package io.fundrequest.core.request.claim.domain;

import io.fundrequest.db.infrastructure.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Table(name = "claim")
@Entity
public class Claim extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "solver")
    private String solver;

    @Column(name = "amount_in_wei")
    private BigDecimal amountInWei;

    @Column(name = "token_hash")
    private String tokenHash;

    @Column(name = "time_stamp")
    private LocalDateTime timestamp;

    @Column(name = "request_id")
    private Long requestId;

    @Column(name="blockchain_event_id")
    private Long blockchainEventId;

    protected Claim() {
    }

    Claim(String solver, BigDecimal amountInWei, String tokenHash, Long requestId, LocalDateTime timestamp, Long blockchainEventId) {
        this.solver = solver;
        this.amountInWei = amountInWei;
        this.tokenHash = tokenHash;
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.blockchainEventId = blockchainEventId;
    }

    void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getSolver() {
        return solver;
    }

    public BigDecimal getAmountInWei() {
        return amountInWei;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public Long getRequestId() {
        return requestId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Long getBlockchainEventId() {
        return blockchainEventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Claim claim = (Claim) o;
        return Objects.equals(solver, claim.solver) &&
               Objects.equals(amountInWei, claim.amountInWei) &&
               Objects.equals(tokenHash, claim.tokenHash) &&
               Objects.equals(timestamp, claim.timestamp) &&
               Objects.equals(requestId, claim.requestId) &&
               Objects.equals(blockchainEventId, claim.blockchainEventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(solver, amountInWei, tokenHash, timestamp, requestId, blockchainEventId);
    }
}
