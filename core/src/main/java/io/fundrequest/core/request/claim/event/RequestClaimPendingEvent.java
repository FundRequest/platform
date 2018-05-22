package io.fundrequest.core.request.claim.event;

import io.fundrequest.core.request.claim.domain.RequestClaim;
import lombok.Builder;
import lombok.Data;

@Data
public class RequestClaimPendingEvent {

    private final RequestClaim requestClaim;

    @Builder
    public RequestClaimPendingEvent(RequestClaim requestClaim) {
        this.requestClaim = requestClaim;
    }
}
