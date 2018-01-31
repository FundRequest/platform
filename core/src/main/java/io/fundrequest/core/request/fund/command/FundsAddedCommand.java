package io.fundrequest.core.request.fund.command;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FundsAddedCommand {

    @NotNull
    private Long requestId;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amountInWei;

    private String transactionId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    private LocalDateTime timestamp;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public BigDecimal getAmountInWei() {
        return amountInWei;
    }

    public void setAmountInWei(BigDecimal amountInWei) {
        this.amountInWei = amountInWei;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
