package io.fundrequest.core.request.claim;

import io.fundrequest.core.request.claim.dto.RequestClaimDto;

import java.security.Principal;
import java.util.List;

public interface ClaimService {
    void claim(Principal user, UserClaimRequest userClaimRequest);

    void approveClaim(Long requestClaimId);

    List<RequestClaimDto> listPendingRequestClaims();

    List<RequestClaimDto> listCompletedRequestClaims();

    void declineClaim(Long requestClaimId);
}
