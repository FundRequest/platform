package io.fundrequest.core.request.claim;

import io.fundrequest.core.request.claim.dto.ClaimsByTransactionAggregate;

import java.security.Principal;

public interface ClaimService {
    void claim(Principal user, UserClaimRequest userClaimRequest);

    ClaimsByTransactionAggregate getAggregatedClaimsForRequest(long requestId);
}
