package io.fundrequest.core.request.claim.command;

import io.fundrequest.core.request.domain.Platform;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestClaimedCommand {
    private Platform platform;
    private String platformId;
    private Long blockchainEventId;
    private String solver;
    private LocalDateTime timestamp;
    private BigDecimal amountInWei;
    private String tokenHash;
}
