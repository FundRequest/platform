package io.fundrequest.core.request.fund.messaging.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FundedEthDto {
    private String transactionHash;
    private String from;
    private String platform;
    private String platformId;
    private String amount;
    private String token;
    private long timestamp;

    FundedEthDto() {
    }

    @Builder
    public FundedEthDto(String transactionHash, String from, String platform, String platformId, String amount, String token, long timestamp) {
        this.transactionHash = transactionHash;
        this.from = from;
        this.platform = platform;
        this.platformId = platformId;
        this.amount = amount;
        this.token = token;
        this.timestamp = timestamp;
    }
}
