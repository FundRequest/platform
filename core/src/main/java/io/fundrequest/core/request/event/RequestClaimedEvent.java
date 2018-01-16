package io.fundrequest.core.request.event;

import io.fundrequest.core.request.view.RequestDto;

import java.time.LocalDateTime;

public class RequestClaimedEvent {
    private RequestDto requestDto;
    private String solver;
    private LocalDateTime timestamp;

    public RequestClaimedEvent(RequestDto requestDto, String solver, LocalDateTime timestamp) {
        this.requestDto = requestDto;
        this.solver = solver;
        this.timestamp = timestamp;
    }

    public RequestDto getRequestDto() {
        return requestDto;
    }

    public String getSolver() {
        return solver;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
