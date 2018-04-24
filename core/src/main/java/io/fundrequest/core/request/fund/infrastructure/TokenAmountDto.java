package io.fundrequest.core.request.fund.infrastructure;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@EqualsAndHashCode
public class TokenAmountDto {
    private final String tokenAddress;
    private final BigDecimal totalAmount;

    public TokenAmountDto(String tokenAddress, BigDecimal totalAmount) {
        this.tokenAddress = tokenAddress;
        this.totalAmount = totalAmount;
    }
}
