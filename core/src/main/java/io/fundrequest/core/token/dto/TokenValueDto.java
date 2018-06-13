package io.fundrequest.core.token.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Data
@Builder
public class TokenValueDto {
    private String tokenAddress;
    private String tokenSymbol;
    private BigDecimal totalAmount;


    public String getTokenImage() {
        if (StringUtils.isNotBlank(tokenSymbol)) {
            return tokenSymbol.toLowerCase() + ".png";
        } else {
            return "";
        }
    }
}
