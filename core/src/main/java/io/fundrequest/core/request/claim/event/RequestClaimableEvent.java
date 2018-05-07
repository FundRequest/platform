package io.fundrequest.core.request.claim.event;

import io.fundrequest.core.request.view.RequestDto;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RequestClaimableEvent {

    private final RequestDto requestDto;
    private final LocalDateTime timestamp;

    public RequestClaimableEvent(final RequestDto requestDto, final LocalDateTime timestamp) {
        this.requestDto = requestDto;
        this.timestamp = timestamp;
    }
}
