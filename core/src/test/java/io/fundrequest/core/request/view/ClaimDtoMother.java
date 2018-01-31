package io.fundrequest.core.request.view;

import io.fundrequest.core.request.claim.domain.ClaimMother;
import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.claim.dto.ClaimDtoMapperImpl;

import java.time.LocalDateTime;
import java.time.Month;

public final class ClaimDtoMother {

    private final static ClaimDtoMapperImpl mapper;

    static {
        mapper = new ClaimDtoMapperImpl();
    }

    public static ClaimDto aClaimDto() {
        return mapper.map(ClaimMother.aClaim()
                .withId(234L)
                .withTimestamp(LocalDateTime.of(2017, Month.DECEMBER, 27, 0, 0))
                .build());
    }

}
