package io.fundrequest.core.request.fund.messaging.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class FundedEthDto {
    private String transactionHash;
    private String logIndex;
    private String from;
    private String platform;
    private String platformId;
    private String token;
    private String amount;
    private long timestamp;

    FundedEthDto() {
    }

    @Builder
    public FundedEthDto(String transactionHash, String logIndex, String from, String platform, String platformId, String amount, String token, long timestamp) {
        this.transactionHash = transactionHash;
        this.logIndex = logIndex;
        this.from = from;
        this.platform = platform;
        this.platformId = platformId;
        this.token = token;
        this.amount = amount;
        this.timestamp = timestamp;
    }
}
