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
        if (StringUtils.isNotBlank(tokenAddress)) {
            return "https://cdn.rawgit.com/TrustWallet/tokens/master/images/" + tokenAddress.toLowerCase() + ".png";
        } else {
            return "";
        }
    }

}
