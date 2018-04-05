package io.fundrequest.core.request.fund.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FundDto {
    private Long id;
    private String funder;
    private BigDecimal amountInWei;
    private String token;
    private Long requestId;
    private LocalDateTime timestamp;

}