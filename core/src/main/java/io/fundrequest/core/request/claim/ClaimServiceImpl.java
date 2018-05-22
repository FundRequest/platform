package io.fundrequest.core.request.claim;

import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.claim.domain.ClaimRequestStatus;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import io.fundrequest.core.request.claim.event.RequestClaimPendingEvent;
import io.fundrequest.core.request.claim.event.RequestClaimedEvent;
import io.fundrequest.core.request.claim.github.GithubClaimResolver;
import io.fundrequest.core.request.claim.infrastructure.RequestClaimRepository;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.view.RequestDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
class ClaimServiceImpl implements ClaimService {

    private RequestRepository requestRepository;
    private RequestClaimRepository requestClaimRepository;
    private GithubClaimResolver githubClaimResolver;
    private Mappers mappers;
    private ApplicationEventPublisher $;

    public ClaimServiceImpl(RequestRepository requestRepository,
                            RequestClaimRepository requestClaimRepository,
                            GithubClaimResolver githubClaimResolver,
                            Mappers mappers,
                            ApplicationEventPublisher eventPublisher) {
        this.requestRepository = requestRepository;
        this.requestClaimRepository = requestClaimRepository;
        this.githubClaimResolver = githubClaimResolver;
        this.mappers = mappers;
        this.$ = eventPublisher;
    }

    @Transactional
    @Override
    public void claim(Principal user, UserClaimRequest userClaimRequest) {
        Request request = requestRepository.findByPlatformAndPlatformId(userClaimRequest.getPlatform(), userClaimRequest.getPlatformId())
                                           .orElseThrow(() -> new RuntimeException("Request not found"));

        RequestDto requestDto = mappers.map(Request.class, RequestDto.class, request);
        String solver = githubClaimResolver.getUserPlatformUsername(user, request.getIssueInformation().getPlatform())
                                           .orElseThrow(() -> new RuntimeException("You are not linked to github"));
        final RequestClaim requestClaim = RequestClaim.builder()
                                                      .address(userClaimRequest.getAddress())
                                                      .requestId(request.getId())
                                                      .flagged(!githubClaimResolver.canClaim(user, requestDto))
                                                      .solver(solver)
                                                      .status(ClaimRequestStatus.PENDING)
                                                      .build();
        requestClaimRepository.save(requestClaim);
        request.setStatus(RequestStatus.CLAIM_REQUESTED);
        $.publishEvent(RequestClaimPendingEvent.builder()
                                               .requestClaim(requestClaim)
                                               .build());
        requestRepository.save(request);
    }


    @EventListener
    public void onClaimed(RequestClaimedEvent claimedEvent) {
        requestClaimRepository.findByRequestId(claimedEvent.getRequestDto().getId())
                              .forEach(requestClaim -> {
                                  requestClaim.setStatus(ClaimRequestStatus.PROCESSED);
                                  requestClaimRepository.save(requestClaim);
                              });
    }
}
