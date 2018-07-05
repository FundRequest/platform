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

    public static TokenValueDto sum(final TokenValueDto tokenValue1, final TokenValueDto tokenValue2) {
        if (tokenValue1 == null && tokenValue2 == null) {
            return null;
        }
        if (tokenValue1 == null) {
            return TokenValueDto.builder()
                                .tokenAddress(tokenValue2.getTokenAddress())
                                .tokenSymbol(tokenValue2.getTokenSymbol())
                                .totalAmount(tokenValue2.getTotalAmount())
                                .build();
        }
        if (tokenValue2 == null) {
            TokenValueDto.builder()
                         .tokenAddress(tokenValue1.getTokenAddress())
                         .tokenSymbol(tokenValue1.getTokenSymbol())
                         .totalAmount(tokenValue1.getTotalAmount())
                         .build();
        }
        if (!tokenValue1.getTokenAddress().equalsIgnoreCase(tokenValue2.getTokenAddress())) {
            throw new RuntimeException("Token address should be the same when summing tokenValueDTOs");
        }
        if (!tokenValue1.getTokenSymbol().equalsIgnoreCase(tokenValue2.getTokenSymbol())) {
            throw new RuntimeException("Token symbol should be the same when summing tokenValueDTOs");
        }
        return TokenValueDto.builder()
                            .tokenAddress(tokenValue1.getTokenAddress())
                            .tokenSymbol(tokenValue1.getTokenSymbol())
                            .totalAmount(tokenValue1.getTotalAmount().add(tokenValue2.getTotalAmount()))
                            .build();
    }
}
