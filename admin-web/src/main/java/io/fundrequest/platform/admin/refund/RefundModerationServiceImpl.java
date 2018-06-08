package io.fundrequest.platform.admin.refund;

import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.fund.domain.RefundRequest;
import io.fundrequest.core.request.fund.domain.RefundRequestStatus;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.core.request.fund.infrastructure.RefundRequestRepository;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.platform.admin.service.ModerationService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service("refundModerationService")
public class RefundModerationServiceImpl implements ModerationService<RefundRequestDto> {

    private final Mappers mappers;
    private final RefundRequestRepository refundRequestRepository;
    private final AzraelClient azraelClient;

    public RefundModerationServiceImpl(final Mappers mappers,
                                       final RefundRequestRepository refundRequestRepository,
                                       final AzraelClient azraelClient) {
        this.mappers = mappers;
        this.refundRequestRepository = refundRequestRepository;
        this.azraelClient = azraelClient;
    }


    @Override
    @Transactional
    public void approve(Long id) {
        // TODO send to Azrael
    }

    @Override
    @Transactional
    public void decline(Long requestRefundId) {
        final RefundRequest refundRequest = refundRequestRepository.findOne(requestRefundId).orElseThrow(() -> new RuntimeException("Refund request not found"));
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

    private List<RefundRequestDto> getRequestRefunds(RefundRequestStatus pending) {
        return mappers.mapList(RefundRequest.class, RefundRequestDto.class, refundRequestRepository.findByStatusIn(Collections.singletonList(pending), new Sort("creationDate")));
    }
}
