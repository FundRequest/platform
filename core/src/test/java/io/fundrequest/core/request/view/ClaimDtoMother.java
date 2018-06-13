package io.fundrequest.core.request.view;

import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.claim.dto.ClaimDtoMapperImpl;
import io.fundrequest.core.token.dto.TokenValueDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;

public final class ClaimDtoMother {

    private final static ClaimDtoMapperImpl mapper;

    static {
        mapper = new ClaimDtoMapperImpl();
    }

    public static ClaimDto.ClaimDtoBuilder aClaimDto() {
        return ClaimDto.builder()
                       .id(234L)
                       .solver("testSolver")
                       .timestamp(LocalDateTime.of(2017, Month.DECEMBER, 27, 0, 0))
                       .tokenValue(TokenValueDto.builder()
                                                .totalAmount(new BigDecimal("1"))
                                                .tokenAddress("0x02f96ef85cad6639500ca1cc8356f0b5ca5bf1d2")
                                                .tokenSymbol("FND")
                                                .build());
    }
}
