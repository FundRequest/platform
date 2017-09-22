package io.fundrequest.core.request.fund.dto;

public class FundDto {

    private String funder;
    private Long amountInWei;
    private Long requestId;

    public String getFunder() {
        return funder;
    }

    public void setFunder(String funder) {
        this.funder = funder;
    }

    public Long getAmountInWei() {
        return amountInWei;
    }

    public void setAmountInWei(Long amountInWei) {
        this.amountInWei = amountInWei;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }
}
