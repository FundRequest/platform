package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.command.RequestRefundCommand;
import io.fundrequest.core.request.fund.domain.RefundRequest;
import io.fundrequest.core.request.fund.domain.RefundRequestStatus;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.core.request.fund.dto.RefundRequestDtoMapper;
import io.fundrequest.core.request.fund.infrastructure.RefundRequestRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RefundServiceImpl implements RefundService {

    private final RefundRequestRepository refundRequestRepository;
    private final RefundRequestDtoMapper refundRequestDtoMapper;

    public RefundServiceImpl(final RefundRequestRepository refundRequestRepository, final RefundRequestDtoMapper refundRequestDtoMapper) {
        this.refundRequestRepository = refundRequestRepository;
        this.refundRequestDtoMapper = refundRequestDtoMapper;
    }

    @Override
    public void requestRefund(final RequestRefundCommand requestRefundCommand) {
        refundRequestRepository.save(RefundRequest.builder()
                                                  .requestId(requestRefundCommand.getRequestId())
                                                  .funderAddress(requestRefundCommand.getFunderAddress())
                                                  .build());
    }

    @Override
    public List<RefundRequestDto> findAllRefundRequestsFor(final long requestId, final RefundRequestStatus status) {
        return refundRequestDtoMapper.mapToList(refundRequestRepository.findAllByRequestIdAndStatus(requestId, status));
    }
}
