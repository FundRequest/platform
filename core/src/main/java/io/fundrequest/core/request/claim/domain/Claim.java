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

@Table(name = "fund")
@Entity
public class Claim extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "solver")
    private String solver;

    @Column(name = "amount_in_wei")
    private BigDecimal amountInWei;

    @Column(name = "time_stamp")
    private LocalDateTime timestamp;

    @Column(name = "request_id")
    private Long requestId;

    protected Claim() {
    }

    Claim(String solver, BigDecimal amountInWei, Long requestId, LocalDateTime timestamp) {
        this.solver = solver;
        this.amountInWei = amountInWei;
        this.requestId = requestId;
        this.timestamp = timestamp;
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

    public Long getRequestId() {
        return requestId;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
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
               Objects.equals(timestamp, claim.timestamp) &&
               Objects.equals(requestId, claim.requestId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(solver, amountInWei, timestamp, requestId);
    }
}
