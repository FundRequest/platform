package io.fundrequest.core.request.claim;

import java.security.Principal;

public interface ClaimService {
    void claim(Principal user, UserClaimRequest userClaimRequest);

    void approveClaim(Long requestClaimId);

    void declineClaim(Long requestClaimId);
}
