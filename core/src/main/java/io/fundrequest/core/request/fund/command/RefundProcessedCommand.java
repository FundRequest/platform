package io.fundrequest.core.request.fund.command;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefundProcessedCommand {

    private final Long requestId;
    private final String funderAddress;
    private final String amount;
    private final String tokenHash;
    private final long blockchainEventId;
    private final String transactionHash;
}