package io.fundrequest.core.request.claim;

import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.claim.dto.ClaimsByTransactionAggregate;

import java.security.Principal;
import java.util.Optional;

public interface ClaimService {

    Optional<ClaimDto> findOne(Long id);

    void claim(Principal user, UserClaimRequest userClaimRequest);

    ClaimsByTransactionAggregate getAggregatedClaimsForRequest(long requestId);
}
