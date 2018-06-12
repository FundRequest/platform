package io.fundrequest.core.token.dto;

import java.math.BigDecimal;

public class TokenValueDtoMother {

    private TokenValueDtoMother() {
    }

    public static TokenValueDto.TokenValueDtoBuilder FND() {
        return TokenValueDto.builder()
                            .tokenAddress("0x9f88c5cc76148d41a5db8d0a7e581481efc9667b")
                            .tokenSymbol("FND")
                            .totalAmount(new BigDecimal("50.33"));
    }

    public static TokenValueDto.TokenValueDtoBuilder ZRX() {
        return TokenValueDto.builder()
                            .tokenAddress("0x6ff6c0ff1d68b964901f986d4c9fa3ac68346570")
                            .tokenSymbol("ZRX")
                            .totalAmount(new BigDecimal("5"));
    }
}
