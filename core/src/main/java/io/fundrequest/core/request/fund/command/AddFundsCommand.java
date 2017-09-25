package io.fundrequest.core.request.fund.command;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

public class AddFundsCommand {

    @NotNull
    private Long requestId;

    @NotNull
    private BigInteger amountInWei;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public BigInteger getAmountInWei() {
        return amountInWei;
    }

    public void setAmountInWei(BigInteger amountInWei) {
        this.amountInWei = amountInWei;
    }
}
