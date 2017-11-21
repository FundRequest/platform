package io.fundrequest.core.request.fund.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class FundDto {
    private Long id;
    private String funder;
    private BigDecimal amountInWei;
    private Long requestId;
    private LocalDateTime creationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFunder() {
        return funder;
    }

    public void setFunder(String funder) {
        this.funder = funder;
    }

    public BigDecimal getAmountInWei() {
        return amountInWei;
    }

    public void setAmountInWei(BigDecimal amountInWei) {
        this.amountInWei = amountInWei;
    }

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }
}
