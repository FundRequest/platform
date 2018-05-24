package io.fundrequest.core.token.model;

import java.math.BigDecimal;

public class TokenValueMother {

    private TokenValueMother() {
    }

    public static TokenValue.TokenValueBuilder FND() {
        return TokenValue.builder()
                         .tokenAddress("0x9f88c5cc76148d41a5db8d0a7e581481efc9667b")
                         .amountInWei(new BigDecimal("50330000000000000000"));
    }

    public static TokenValue.TokenValueBuilder ZRX() {
        return TokenValue.builder()
                         .tokenAddress("0x6ff6c0ff1d68b964901f986d4c9fa3ac68346570")
                         .amountInWei(new BigDecimal("5000000000000000000"));
    }
}
