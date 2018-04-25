package io.fundrequest.core.request.fund.command;

import io.fundrequest.core.request.domain.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingFundCommand {

    private String description;
    private String transactionId;
    private String fromAddress;
    private String amount;
    private String tokenAddress;
    private Platform platform;
    private String platformId;
}
