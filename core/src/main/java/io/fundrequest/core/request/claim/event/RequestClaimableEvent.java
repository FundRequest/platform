package io.fundrequest.core.request.claim.event;

import io.fundrequest.core.request.view.RequestDto;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RequestClaimableEvent {

    private final RequestDto requestDto;
    private final LocalDateTime timestamp;
}
