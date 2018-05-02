package io.fundrequest.platform.admin.claim;

import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.claim.domain.ClaimRequestStatus;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import io.fundrequest.core.request.claim.dto.RequestClaimDto;
import io.fundrequest.core.request.claim.infrastructure.RequestClaimRepository;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.request.infrastructure.azrael.ClaimSignature;
import io.fundrequest.core.request.infrastructure.azrael.SignClaimCommand;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ClaimModerationServiceImpl implements ClaimModerationService {

    private final Mappers mappers;
    private final RabbitTemplate approvedClaimRabbitTemplate;
    private final RequestClaimRepository requestClaimRepository;
    private final RequestRepository requestRepository;
    private final AzraelClient azraelClient;

    public ClaimModerationServiceImpl(Mappers mappers,
                                      RabbitTemplate approvedClaimRabbitTemplate,
                                      RequestClaimRepository requestClaimRepository,
                                      RequestRepository requestRepository,
                                      AzraelClient azraelClient) {
        this.mappers = mappers;
        this.approvedClaimRabbitTemplate = approvedClaimRabbitTemplate;
        this.requestClaimRepository = requestClaimRepository;
        this.requestRepository = requestRepository;
        this.azraelClient = azraelClient;
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

    private SignClaimCommand createSignClaimCommand(RequestClaim requestClaim, Request request) {
        return SignClaimCommand.builder()
                               .platform(request.getIssueInformation().getPlatform().name())
                               .platformId(request.getIssueInformation().getPlatformId())
                               .solver(requestClaim.getSolver())
                               .address(requestClaim.getAddress())
                               .build();
    }
}
