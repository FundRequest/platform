package io.fundrequest.core.request.claim;

import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.claim.domain.ClaimRequestStatus;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import io.fundrequest.core.request.claim.dto.RequestClaimDto;
import io.fundrequest.core.request.claim.github.GithubClaimResolver;
import io.fundrequest.core.request.claim.infrastructure.RequestClaimRepository;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.request.infrastructure.azrael.ClaimSignature;
import io.fundrequest.core.request.infrastructure.azrael.SignClaimCommand;
import io.fundrequest.core.request.view.RequestDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
class ClaimServiceImpl implements ClaimService {

    private RequestRepository requestRepository;
    private RequestClaimRepository requestClaimRepository;
    private GithubClaimResolver githubClaimResolver;
    private Mappers mappers;
    private RabbitTemplate approvedClaimRabbitTemplate;
    private AzraelClient azraelClient;

    public ClaimServiceImpl(RequestRepository requestRepository,
                            RequestClaimRepository requestClaimRepository,
                            GithubClaimResolver githubClaimResolver,
                            Mappers mappers,
                            RabbitTemplate approvedClaimRabbitTemplate,
                            AzraelClient azraelClient) {
        this.requestRepository = requestRepository;
        this.requestClaimRepository = requestClaimRepository;
        this.githubClaimResolver = githubClaimResolver;
        this.mappers = mappers;
        this.approvedClaimRabbitTemplate = approvedClaimRabbitTemplate;
        this.azraelClient = azraelClient;
    }

    @Transactional
    @Override
    public void claim(Principal user, UserClaimRequest userClaimRequest) {
        Request request = requestRepository.findByPlatformAndPlatformId(userClaimRequest.getPlatform(), userClaimRequest.getPlatformId())
                                           .orElseThrow(() -> new RuntimeException("Request not found"));

        RequestDto requestDto = mappers.map(Request.class, RequestDto.class, request);
        String solver = githubClaimResolver.getUserPlatformUsername(user, request.getIssueInformation().getPlatform())
                                           .orElseThrow(() -> new RuntimeException("You are not linked to github"));
        RequestClaim requestClaim = RequestClaim.builder()
                                                .address(userClaimRequest.getAddress())
                                                .requestId(request.getId())
                                                .flagged(!githubClaimResolver.canClaim(user, requestDto))
                                                .solver(solver)
                                                .status(ClaimRequestStatus.PENDING)
                                                .build();
        requestClaimRepository.save(requestClaim);
        request.setStatus(RequestStatus.CLAIM_REQUESTED);
        requestRepository.save(request);
    }

    @Transactional
    @Override
    public void approveClaim(Long requestClaimId) {
        RequestClaim requestClaim = requestClaimRepository.findOne(requestClaimId).orElseThrow(() -> new RuntimeException("Request claim not found"));
        Request request = requestRepository.findOne(requestClaim.getRequestId()).get();
        ClaimSignature sig = azraelClient.getSignature(createSignClaimCommand(requestClaim, request));
        approvedClaimRabbitTemplate.convertAndSend(sig);
        request.setStatus(RequestStatus.CLAIM_APPROVED);
        requestClaim.setStatus(ClaimRequestStatus.APPROVED);
        requestRepository.save(request);
        requestClaimRepository.save(requestClaim);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestClaimDto> listPendingRequestClaims() {
        return mappers.mapList(
                RequestClaim.class,
                RequestClaimDto.class,
                requestClaimRepository.findByStatusIn(Collections.singletonList(ClaimRequestStatus.PENDING), new Sort("creationDate"))
                              );
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestClaimDto> listCompletedRequestClaims() {
        List<ClaimRequestStatus> statuses = Arrays.asList(ClaimRequestStatus.values());
        statuses.remove(ClaimRequestStatus.PENDING);
        return mappers.mapList(
                RequestClaim.class,
                RequestClaimDto.class,
                requestClaimRepository.findByStatusIn(statuses, new Sort(new Sort.Order(Sort.Direction.DESC, "lastModifiedDate")))
                              );
    }


    private SignClaimCommand createSignClaimCommand(RequestClaim requestClaim, Request request) {
        return SignClaimCommand.builder()
                               .platform(request.getIssueInformation().getPlatform().name())
                               .platformId(request.getIssueInformation().getPlatformId())
                               .solver(requestClaim.getSolver())
                               .address(requestClaim.getAddress())
                               .build();
    }


    @Transactional
    @Override
    public void declineClaim(Long requestClaimId) {
        RequestClaim requestClaim = requestClaimRepository.findOne(requestClaimId).orElseThrow(() -> new RuntimeException("Request claim not found"));
        Request request = requestRepository.findOne(requestClaim.getRequestId()).get();
        request.setStatus(RequestStatus.FUNDED);
        requestClaim.setStatus(ClaimRequestStatus.DECLINED);
        requestRepository.save(request);
        requestClaimRepository.save(requestClaim);
    }
}
