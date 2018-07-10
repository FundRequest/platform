package io.fundrequest.core.request.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestStatus {
    OPEN(RequestPhase.OPEN),
    FUNDED(RequestPhase.OPEN),
    CLAIMABLE(RequestPhase.RESOLVED),
    CLAIM_REQUESTED(RequestPhase.RESOLVED),
    CLAIM_APPROVED(RequestPhase.RESOLVED),
    CLAIMED(RequestPhase.CLOSED),

    IN_PROGRESS(RequestPhase.OPEN),
    CLOSED(RequestPhase.CLOSED),
    CANCELLED(RequestPhase.CLOSED),
    RESOLVED(RequestPhase.RESOLVED),
    UNRESOLVED(RequestPhase.OPEN);

    private final RequestPhase phase;
}
