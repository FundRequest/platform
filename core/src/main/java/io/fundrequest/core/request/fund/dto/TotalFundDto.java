package io.fundrequest.core.request.fund.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TotalFundDto {
    private String tokenAddress;
    private String tokenSymbol;
    private BigDecimal totalAmount;
}
