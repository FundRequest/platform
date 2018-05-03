package io.fundrequest.core.request.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RequestStatus {
    OPEN(RequestFase.OPEN),
    FUNDED(RequestFase.OPEN),
    CLAIMABLE(RequestFase.RESOLVED),
    CLAIM_REQUESTED(RequestFase.RESOLVED),
    CLAIM_APPROVED(RequestFase.RESOLVED),
    CLAIMED(RequestFase.CLOSED),

    IN_PROGRESS(RequestFase.OPEN),
    CLOSED(RequestFase.CLOSED),
    CANCELLED(RequestFase.CLOSED),
    RESOLVED(RequestFase.RESOLVED),
    UNRESOLVED(RequestFase.OPEN);

    private final RequestFase fase;
}
