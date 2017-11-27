package io.fundrequest.core.request.event;

import io.fundrequest.core.request.view.RequestDto;

public class RequestCreatedEvent {

    private RequestDto requestDto;

    public RequestCreatedEvent(RequestDto requestDto) {
        this.requestDto = requestDto;
    }


    public RequestDto getRequestDto() {
        return requestDto;
    }
}
