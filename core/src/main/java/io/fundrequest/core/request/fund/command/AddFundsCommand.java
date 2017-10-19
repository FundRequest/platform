package io.fundrequest.core.request.fund.command;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class AddFundsCommand {

    @NotNull
    private Long requestId;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amountInWei;

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
}
