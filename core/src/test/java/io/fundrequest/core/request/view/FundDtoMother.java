package io.fundrequest.core.request.view;

import io.fundrequest.core.request.domain.FundMother;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.dto.FundDtoMapperImpl;

public final class FundDtoMother {

    private final static FundDtoMapperImpl mapper;

    static {
        mapper = new FundDtoMapperImpl();
    }

    public static FundDto aFundDto() {
        return mapper.map(FundMother.aFund().build());
    }

}
