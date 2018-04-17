package io.fundreqest.platform.tweb.fund.command;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PendingFundDto {

    private String description;
    private String transactionId;
    private String fromAddress;
    private String amount;
    private String tokenAddress;
}
