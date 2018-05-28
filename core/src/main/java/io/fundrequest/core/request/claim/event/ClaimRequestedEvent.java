package io.fundrequest.core.request.claim.event;

import io.fundrequest.core.request.claim.domain.RequestClaim;
import lombok.Builder;
import lombok.Data;

@Data
public class ClaimRequestedEvent {

    private final RequestClaim requestClaim;

    @Builder
    public ClaimRequestedEvent(RequestClaim requestClaim) {
        this.requestClaim = requestClaim;
    }
}
