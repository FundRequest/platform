package io.fundrequest.core.request.domain;

public enum RequestStatus {
    OPEN,
    FUNDED,
    CLAIM_REQUESTED,
    CLAIMABLE,
    CLAIMED,
    CLAIM_APPROVED,
    IN_PROGRESS,
    CLOSED,
    CANCELLED,
    RESOLVED,
    UNRESOLVED
}
