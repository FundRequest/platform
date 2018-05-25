package io.fundrequest.core.request.fund.messaging.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClaimedEthDto {
    private String transactionHash;
    private String logIndex;
    private String solverAddress;
    private String platform;
    private String platformId;
    private String solver;
    private String token;
    private String amount;
    private long timestamp;
}
