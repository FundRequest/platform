package io.fundrequest.core.request.claim;

import io.fundrequest.core.request.claim.dto.ClaimsAggregate;

import java.security.Principal;

public interface ClaimService {
    void claim(Principal user, UserClaimRequest userClaimRequest);

    ClaimsAggregate getAggregatedClaimsForRequest(long requestId);
}
