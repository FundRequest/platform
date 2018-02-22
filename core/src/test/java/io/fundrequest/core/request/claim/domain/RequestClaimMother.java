package io.fundrequest.core.request.claim.domain;

public final class RequestClaimMother {

    public static RequestClaim.RequestClaimBuilder requestClaim() {
        return RequestClaim.builder()
                .id(1L)
                .status(ClaimRequestStatus.PENDING)
                .solver("davyvanroy")
                .requestId(1L)
                .address("0x0")
                .flagged(false);
    }

}