package io.fundrequest.core.request;

import io.fundrequest.core.request.claim.CanClaimRequest;
import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.claim.UserClaimRequest;
import io.fundrequest.core.request.claim.command.RequestClaimedCommand;
import io.fundrequest.core.request.claim.dto.UserClaimableDto;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.fund.CreateERC67FundRequest;
import io.fundrequest.core.request.fund.dto.CommentDto;
import io.fundrequest.core.request.view.RequestDto;

import java.security.Principal;
import java.util.List;
import java.util.Set;

public interface RequestService {
    List<RequestDto> findAll();

    List<RequestDto> findAll(Iterable<Long> ids);

    Set<String> findAllTechnologies();

    Set<String> findAllProjects();

    UserClaimableDto getUserClaimableResult(Principal principal, Long id);

    List<RequestDto> findRequestsForUser(Principal principal);

    RequestDto findRequest(Long id);

    List<CommentDto> getComments(Long requestId);

    RequestDto findRequest(Platform platform, String platformId);

    Long createRequest(CreateRequestCommand command);

    void requestClaimed(RequestClaimedCommand command);

    SignedClaim signClaimRequest(Principal principal, UserClaimRequest userClaimRequest);

    Boolean canClaim(Principal user, CanClaimRequest canClaimRequest);

    void addWatcherToRequest(Principal principal, Long requestId);

    void removeWatcherFromRequest(Principal principal, Long requestId);

    String generateERC67(CreateERC67FundRequest createERC67FundRequest);
}
