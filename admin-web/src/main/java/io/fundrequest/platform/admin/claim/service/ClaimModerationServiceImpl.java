package io.fundrequest.platform.admin.claim.service;

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
import io.fundrequest.core.request.infrastructure.azrael.ClaimTransaction;
import io.fundrequest.core.request.infrastructure.azrael.SignClaimCommand;
import io.fundrequest.platform.admin.service.ModerationService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static io.fundrequest.core.web3j.AddressUtils.prettify;

@Service("claimModerationService")
public class ClaimModerationServiceImpl implements ModerationService<RequestClaimDto> {

    private final Mappers mappers;
    private final RequestClaimRepository requestClaimRepository;
    private final RequestRepository requestRepository;
    private final AzraelClient azraelClient;

    public ClaimModerationServiceImpl(final Mappers mappers,
                                      final RequestClaimRepository requestClaimRepository,
                                      final RequestRepository requestRepository,
                                      final AzraelClient azraelClient) {
        this.mappers = mappers;
        this.requestClaimRepository = requestClaimRepository;
        this.requestRepository = requestRepository;
        this.azraelClient = azraelClient;
    }


    @Transactional
    @Override
    public void approve(Long requestClaimId) {
        RequestClaim requestClaim = requestClaimRepository.findOne(requestClaimId).orElseThrow(() -> new RuntimeException("Request claim not found"));
        Request request = requestRepository.findOne(requestClaim.getRequestId()).get();
        ClaimSignature sig = azraelClient.getSignature(createSignClaimCommand(requestClaim, request));
        try {
            final ClaimTransaction claimTransaction = azraelClient.submitClaim(sig);
            request.setStatus(RequestStatus.CLAIM_APPROVED);
            requestClaim.setStatus(ClaimRequestStatus.APPROVED);
            requestClaim.setTransactionHash(prettify(claimTransaction.getTransactionHash()));
            requestClaim.setTransactionSubmitTime(LocalDateTime.now());
            requestRepository.save(request);
            requestClaimRepository.save(requestClaim);
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to submit claim transaction: " + ex.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestClaimDto> listPending() {
        return getRequestClaims(ClaimRequestStatus.PENDING);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestClaimDto> listFailed() {
        return getRequestClaims(ClaimRequestStatus.TRANSACTION_FAILED);
    }

    @Transactional
    @Override
    public void decline(Long requestClaimId) {
        RequestClaim requestClaim = requestClaimRepository.findOne(requestClaimId).orElseThrow(() -> new RuntimeException("Request claim not found"));
        Request request = requestRepository.findOne(requestClaim.getRequestId()).get();
        request.setStatus(RequestStatus.FUNDED);
        requestClaim.setStatus(ClaimRequestStatus.DECLINED);
        requestRepository.save(request);
        requestClaimRepository.save(requestClaim);
    }

    private List<RequestClaimDto> getRequestClaims(ClaimRequestStatus pending) {
        return mappers.mapList(
                RequestClaim.class,
                RequestClaimDto.class,
                requestClaimRepository.findByStatusIn(Collections.singletonList(pending), new Sort("creationDate"))
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
}
