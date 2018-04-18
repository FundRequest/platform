package io.fundrequest.core.request.claim.command;

import io.fundrequest.core.request.domain.Platform;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class RequestClaimedCommand {
    private Platform platform;
    private String platformId;
    private String transactionId;
    private String solver;
    private LocalDateTime timestamp;
    private BigDecimal amountInWei;

    public RequestClaimedCommand() {
    }

    public RequestClaimedCommand(Platform platform, String platformId, String transactionId, String solver, LocalDateTime timestamp, BigDecimal amountInWei) {
        this.platform = platform;
        this.platformId = platformId;
        this.transactionId = transactionId;
        this.solver = solver;
        this.timestamp = timestamp;
        this.amountInWei = amountInWei;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getSolver() {
        return solver;
    }

    public void setSolver(String solver) {
        this.solver = solver;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public BigDecimal getAmountInWei() {
        return amountInWei;
    }

    public void setAmountInWei(BigDecimal amountInWei) {
        this.amountInWei = amountInWei;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RequestClaimedCommand that = (RequestClaimedCommand) o;
        return platform == that.platform &&
               Objects.equals(platformId, that.platformId) &&
               Objects.equals(transactionId, that.transactionId) &&
               Objects.equals(solver, that.solver) &&
               Objects.equals(timestamp, that.timestamp) &&
               Objects.equals(amountInWei, that.amountInWei);
    }

    @Override
    public int hashCode() {

        return Objects.hash(platform, platformId, transactionId, solver, timestamp, amountInWei);
    }
}
