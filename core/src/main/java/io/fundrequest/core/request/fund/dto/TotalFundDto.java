package io.fundrequest.core.request.fund.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Data
@Builder
public class TotalFundDto {
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
