package io.fundrequest.core.request.view;

import io.fundrequest.core.request.domain.BlockchainEvent;
import io.fundrequest.core.request.domain.FundMother;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.dto.FundDtoMapperImpl;
import io.fundrequest.core.request.mapper.BlockchainEventDtoMapperImpl;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.Month;

public final class FundDtoMother {

    private final static FundDtoMapperImpl mapper;

    static {
        mapper = new FundDtoMapperImpl();
        ReflectionTestUtils.setField(mapper, "blockchainEventDtoMapper", new BlockchainEventDtoMapperImpl());
    }

    public static FundDto aFundDto() {
        return mapper.map(FundMother.fndFundFunderKnown()
                                    .timestamp(LocalDateTime.of(2017, Month.DECEMBER, 27, 0, 0))
                                    .blockchainEvent(new BlockchainEvent("0x22a509e9eab34509c828f75283472817eb38abcc24f4f3cff05fac570f4da962", "0x011"))
                                    .build());
    }

}
