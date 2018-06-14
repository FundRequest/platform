package io.fundrequest.core.token.model;

import io.fundrequest.core.token.dto.TokenInfoDtoMother;

import java.math.BigDecimal;

public class TokenValueMother {

    private TokenValueMother() {
    }

    public static TokenValue.TokenValueBuilder FND() {
        return TokenValue.builder()
                         .tokenAddress(TokenInfoDtoMother.fnd().getAddress())
                         .amountInWei(new BigDecimal("50330000000000000000"));
    }

    public static TokenValue.TokenValueBuilder ZRX() {
        return TokenValue.builder()
                         .tokenAddress(TokenInfoDtoMother.zrx().getAddress())
                         .amountInWei(new BigDecimal("5000000000000000000"));
    }
}
