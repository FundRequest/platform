package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.token.dto.TokenValueDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDto {
    private Long id;
    private String solver;
    private TokenValueDto tokenValue;
    private Long requestId;
    private LocalDateTime timestamp;
    private Long blockchainEventId;
    private String transactionHash;
}
