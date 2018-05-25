package io.fundrequest.core.request.claim.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ClaimDto {
    private Long id;
    private String solver;
    private BigDecimal amountInWei;
    private String tokenHash;
    private Long requestId;
    private LocalDateTime timestamp;
    private Long blockchainEventId;
}
