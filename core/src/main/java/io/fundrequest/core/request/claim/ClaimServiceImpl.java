package io.fundrequest.core.request.claim;

import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.claim.domain.Claim;
import io.fundrequest.core.request.claim.domain.ClaimRequestStatus;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.claim.dto.ClaimsByTransactionAggregate;
import io.fundrequest.core.request.claim.event.ClaimRequestedEvent;
import io.fundrequest.core.request.claim.event.RequestClaimedEvent;
import io.fundrequest.core.request.claim.github.GithubClaimResolver;
import io.fundrequest.core.request.claim.infrastructure.ClaimRepository;
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

    private final RequestRepository requestRepository;
    private final ClaimRepository claimRepository;
    private final RequestClaimRepository requestClaimRepository;
    private final GithubClaimResolver githubClaimResolver;
    private final Mappers mappers;
    private final ClaimDtoAggregator claimDtoAggregator;
    private final ApplicationEventPublisher eventPublisher;

    public ClaimServiceImpl(final RequestRepository requestRepository,
                            final ClaimRepository claimRepository,
                            final RequestClaimRepository requestClaimRepository,
                            final GithubClaimResolver githubClaimResolver,
                            final Mappers mappers,
                            final ClaimDtoAggregator claimDtoAggregator,
                            final ApplicationEventPublisher eventPublisher) {
        this.requestRepository = requestRepository;
        this.claimRepository = claimRepository;
        this.requestClaimRepository = requestClaimRepository;
        this.githubClaimResolver = githubClaimResolver;
        this.mappers = mappers;
        this.claimDtoAggregator = claimDtoAggregator;
        this.eventPublisher = eventPublisher;
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
        eventPublisher.publishEvent(ClaimRequestedEvent.builder()
                                                       .requestClaim(requestClaim)
                                                       .build());
        requestRepository.save(request);
    }

    @Override
    @Transactional(readOnly = true)
    public ClaimsByTransactionAggregate getAggregatedClaimsForRequest(final long requestId) {
        return claimDtoAggregator.aggregateClaims(mappers.mapList(Claim.class, ClaimDto.class, claimRepository.findByRequestId(requestId)));
    }

    @EventListener
    public void onClaimed(final RequestClaimedEvent claimedEvent) {
        requestClaimRepository.findByRequestId(claimedEvent.getRequestDto().getId())
                              .forEach(requestClaim -> {
                                  requestClaim.setStatus(ClaimRequestStatus.PROCESSED);
                                  requestClaimRepository.save(requestClaim);
                              });
    }
}
