package io.fundrequest.core.request.claim.event;

import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.view.RequestDto;

import java.time.LocalDateTime;

public class RequestClaimedEvent {
    private RequestDto requestDto;
    private ClaimDto claimDto;
    private String solver;
    private LocalDateTime timestamp;

    public RequestClaimedEvent(RequestDto requestDto, ClaimDto claimDto, String solver, LocalDateTime timestamp) {
        this.requestDto = requestDto;
        this.claimDto = claimDto;
        this.solver = solver;
        this.timestamp = timestamp;
    }

    public RequestDto getRequestDto() {
        return requestDto;
    }

    public ClaimDto getClaimDto() {
        return claimDto;
    }

    public String getSolver() {
        return solver;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
