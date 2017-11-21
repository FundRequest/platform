package io.fundrequest.core.request.fund.event;

import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.view.RequestDto;

public class RequestFundedEvent {
    private FundDto fundDto;
    private RequestDto requestDto;

    public RequestFundedEvent(FundDto fundDto, RequestDto requestDto) {
        this.fundDto = fundDto;
        this.requestDto = requestDto;
    }

    public FundDto getFundDto() {
        return fundDto;
    }

    public RequestDto getRequestDto() {
        return requestDto;
    }
}
