package io.fundrequest.core.request;

import io.fundrequest.core.request.claim.SignClaimRequest;
import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.command.RequestClaimedCommand;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.fund.CreateERC67FundRequest;
import io.fundrequest.core.request.view.RequestDto;

import java.security.Principal;
import java.util.List;

public interface RequestService {
    List<RequestDto> findAll();

    List<RequestDto> findAll(Iterable<Long> ids);

    List<RequestDto> findRequestsForUser(Principal principal);

    RequestDto findRequest(Long id);

    RequestDto findRequest(Platform platform, String platformId);

    RequestDto createRequest(CreateRequestCommand command);

    void requestClaimed(RequestClaimedCommand command);

    SignedClaim signClaimRequest(Principal principal, SignClaimRequest signClaimRequest);

    void addWatcherToRequest(Principal principal, Long requestId);

    void removeWatcherFromRequest(Principal principal, Long requestId);

    String generateERC67(CreateERC67FundRequest createERC67FundRequest);
}
