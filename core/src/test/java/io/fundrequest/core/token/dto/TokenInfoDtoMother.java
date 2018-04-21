package io.fundrequest.core.token.dto;

public final class TokenInfoDtoMother {

    public static TokenInfoDto fnd() {
        return TokenInfoDto.builder()
                           .address("0x9f88c5cc76148d41a5db8d0a7e581481efc9667b")
                           .name("FundRequest")
                           .symbol("FND")
                           .decimals(18)
                           .build();
    }

    public static TokenInfoDto zrx() {
        return TokenInfoDto.builder()
                           .address("0x6ff6c0ff1d68b964901f986d4c9fa3ac68346570")
                           .name("0x")
                           .symbol("ZRX")
                           .decimals(18)
                           .build();
    }

}