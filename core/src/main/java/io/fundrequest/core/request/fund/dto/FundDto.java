package io.fundrequest.core.request.fund.dto;

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
public class FundDto {
    private Long id;
    private String funderAddress;
    private TokenValueDto tokenValue;
    private Long requestId;
    private LocalDateTime timestamp;
    private Long blockchainEventId;
}