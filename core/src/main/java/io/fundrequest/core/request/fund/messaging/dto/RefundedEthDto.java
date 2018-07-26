package io.fundrequest.core.request.fund.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundedEthDto {

    private String transactionHash;
    private String logIndex;
    private String owner;
    private String platform;
    private String platformId;
    private String token;
    private String amount;
    private long timestamp;
}