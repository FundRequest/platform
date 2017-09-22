package io.fundrequest.core.request.fund.command;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class AddFundsCommand {

    @NotNull
    private Long requestId;

    @NotNull
    @Min(1)
    private Long amountInWei;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getAmountInWei() {
        return amountInWei;
    }

    public void setAmountInWei(Long amountInWei) {
        this.amountInWei = amountInWei;
    }
}
