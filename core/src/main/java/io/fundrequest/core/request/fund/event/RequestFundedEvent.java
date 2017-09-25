package io.fundrequest.core.request.fund.event;

import java.math.BigInteger;

public class RequestFundedEvent {
    private Long requestId;
    private String owner;
    private String repo;
    private String number;
    private String funder;
    private BigInteger amountInWei;

    public RequestFundedEvent(Long requestId, String owner, String repo, String number, String funder, BigInteger amountInWei) {
        this.requestId = requestId;
        this.owner = owner;
        this.repo = repo;
        this.number = number;
        this.funder = funder;
        this.amountInWei = amountInWei;
    }

    public Long getRequestId() {
        return requestId;
    }

    public String getOwner() {
        return owner;
    }

    public String getRepo() {
        return repo;
    }

    public String getNumber() {
        return number;
    }

    public String getFunder() {
        return funder;
    }

    public BigInteger getAmountInWei() {
        return amountInWei;
    }
}
