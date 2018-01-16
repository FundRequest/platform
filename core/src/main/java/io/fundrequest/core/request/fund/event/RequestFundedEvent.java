package io.fundrequest.core.request.fund.event;

import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.view.RequestDto;

import java.time.LocalDateTime;

public class RequestFundedEvent {
    private FundDto fundDto;
    private RequestDto requestDto;
    private LocalDateTime timestamp;

    public RequestFundedEvent(FundDto fundDto, RequestDto requestDto, LocalDateTime timestamp) {
        this.fundDto = fundDto;
        this.requestDto = requestDto;
        this.timestamp = timestamp;
    }

    public FundDto getFundDto() {
        return fundDto;
    }

    public RequestDto getRequestDto() {
        return requestDto;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
