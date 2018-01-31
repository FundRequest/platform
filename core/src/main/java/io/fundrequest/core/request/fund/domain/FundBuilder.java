package io.fundrequest.core.request.fund.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class FundBuilder {
    private String funder;
    private BigDecimal amountInWei;
    private Long requestId;
    private Long id;
    private LocalDateTime timestamp;

    private FundBuilder() {
    }

    public static FundBuilder aFund() {
        return new FundBuilder();
    }

    public FundBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public FundBuilder withFunder(String funder) {
        this.funder = funder;
        return this;
    }

    public FundBuilder withAmountInWei(BigDecimal amountInWei) {
        this.amountInWei = amountInWei;
        return this;
    }

    public FundBuilder withRequestId(Long requestId) {
        this.requestId = requestId;
        return this;
    }

    public Fund build() {
        Fund fund = new Fund(funder, amountInWei, requestId, timestamp);
        fund.setId(this.id);
        return fund;
    }

    public FundBuilder withTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
