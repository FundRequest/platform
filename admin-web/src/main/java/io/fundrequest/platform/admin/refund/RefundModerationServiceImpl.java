package io.fundrequest.platform.admin.refund;

import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.fund.domain.RefundRequest;
import io.fundrequest.core.request.fund.domain.RefundRequestStatus;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.core.request.fund.infrastructure.RefundRequestRepository;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.request.infrastructure.azrael.RefundCommand;
import io.fundrequest.core.request.infrastructure.azrael.RefundTransaction;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.platform.admin.service.ModerationService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service("refundModerationService")
public class RefundModerationServiceImpl implements ModerationService<RefundRequestDto> {

    private final Mappers mappers;
    private final RefundRequestRepository refundRequestRepository;
    private final AzraelClient azraelClient;
    private final RequestService requestService;

    public RefundModerationServiceImpl(final Mappers mappers,
                                       final RefundRequestRepository refundRequestRepository,
                                       final AzraelClient azraelClient,
                                       final RequestService requestService) {
        this.mappers = mappers;
        this.refundRequestRepository = refundRequestRepository;
        this.azraelClient = azraelClient;
        this.requestService = requestService;
    }

    @Override
    @Transactional
    public void approve(final Long refundRequestId) {
        final RefundRequest refundRequest = refundRequestRepository.findOne(refundRequestId).orElseThrow(() -> new RuntimeException("Refund request not found"));
        refundRequest.setStatus(RefundRequestStatus.APPROVED);
        refundRequest.setTransactionSubmitTime(LocalDateTime.now());
        final IssueInformationDto issueInformation = requestService.findRequest(refundRequest.getRequestId()).getIssueInformation();
        final RefundTransaction refundTransaction = azraelClient.submitRefund(RefundCommand.builder()
                                                                                           .address(refundRequest.getFunderAddress())
                                                                                           .platform(issueInformation.getPlatform().name())
                                                                                           .platformId(issueInformation.getPlatformId())
                                                                                           .build());
        refundRequest.setTransactionHash(refundTransaction.getTransactionHash());
        refundRequestRepository.save(refundRequest);
    }

    @Override
    @Transactional
    public void decline(final Long refundRequestId) {
        final RefundRequest refundRequest = refundRequestRepository.findOne(refundRequestId).orElseThrow(() -> new RuntimeException("Refund request not found"));
        refundRequest.setStatus(RefundRequestStatus.DECLINED);
        refundRequestRepository.save(refundRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundRequestDto> listPending() {
        return getRequestRefunds(RefundRequestStatus.PENDING);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundRequestDto> listFailed() {
        return getRequestRefunds(RefundRequestStatus.TRANSACTION_FAILED);
    }

    private List<RefundRequestDto> getRequestRefunds(final RefundRequestStatus pending) {
        return mappers.mapList(RefundRequest.class, RefundRequestDto.class, refundRequestRepository.findAllByStatusIn(Collections.singletonList(pending), new Sort("creationDate")));
    }
}
