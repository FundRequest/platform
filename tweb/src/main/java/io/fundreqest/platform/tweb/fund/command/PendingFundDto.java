package io.fundreqest.platform.tweb.fund.command;

import io.fundrequest.core.fund.domain.PendingFund;
import io.fundrequest.core.request.domain.Platform;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.security.Principal;

@Data
@NoArgsConstructor
public class PendingFundDto {

    private String description;
    private String transactionId;
    private String fromAddress;
    private String amount;
    private String tokenAddress;
    private Platform platform;
    private String platformId;

    public PendingFund toPendingFund(final Principal principal) {
        return PendingFund.builder()
                .amount(new BigInteger(amount))
                .description(description)
                .fromAddress(fromAddress)
                .tokenAddress(tokenAddress)
                .transactionhash(transactionId)
                .platform(platform)
                .platformId(platformId)
                .userId(principal.getName())
                .build();
    }
}
