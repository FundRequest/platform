package io.fundrequest.platform.admin.claim.service;

import io.fundrequest.core.request.claim.dto.RequestClaimDto;

import java.util.List;

public interface ClaimModerationService {

    void approveClaim(Long requestClaimId);
    List<RequestClaimDto> listPendingRequestClaims();
    List<RequestClaimDto> listFailedRequestClaims();
    List<RequestClaimDto> listCompletedRequestClaims();
    void declineClaim(Long requestClaimId);
}
