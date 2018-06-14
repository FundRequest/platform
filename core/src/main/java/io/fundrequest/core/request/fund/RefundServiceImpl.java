package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.command.RefundProcessedCommand;
import io.fundrequest.core.request.fund.command.RequestRefundCommand;
import io.fundrequest.core.request.fund.domain.Refund;
import io.fundrequest.core.request.fund.domain.RefundRequest;
import io.fundrequest.core.request.fund.domain.RefundRequestStatus;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.core.request.fund.dto.RefundRequestDtoMapper;
import io.fundrequest.core.request.fund.infrastructure.RefundRepository;
import io.fundrequest.core.request.fund.infrastructure.RefundRequestRepository;
import io.fundrequest.core.token.model.TokenValue;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.APPROVED;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.PROCESSED;

@Service
public class RefundServiceImpl implements RefundService {

    private final RefundRequestRepository refundRequestRepository;
    private final RefundRepository refundRepository;
    private final RefundRequestDtoMapper refundRequestDtoMapper;

    public RefundServiceImpl(final RefundRequestRepository refundRequestRepository,
                             final RefundRepository refundRepository,
                             final RefundRequestDtoMapper refundRequestDtoMapper) {
        this.refundRequestRepository = refundRequestRepository;
        this.refundRepository = refundRepository;
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

    @Override
    public List<RefundRequestDto> findAllRefundRequestsFor(final long requestId, final String funderAddress, final RefundRequestStatus status) {
        return refundRequestDtoMapper.mapToList(refundRequestRepository.findAllByRequestIdAndFunderAddressAndStatus(requestId, funderAddress, status));
    }

    @Override
    public void refundProcessed(final RefundProcessedCommand command) {
        refundRepository.save(Refund.builder()
                                    .requestId(command.getRequestId())
                                    .funderAddress(command.getFunderAddress())
                                    .blockchainEventId(command.getBlockchainEventId())
                                    .tokenValue(TokenValue.builder()
                                                          .amountInWei(new BigDecimal(command.getAmount()))
                                                          .tokenAddress(command.getTokenHash())
                                                          .build())
                                    .build());

        final List<RefundRequest> refundRequests = refundRequestRepository.findAllByRequestIdAndStatus(command.getRequestId(), APPROVED);
        refundRequests.forEach(refundRequest -> {
            refundRequest.setStatus(PROCESSED);
            refundRequest.setTransactionHash(command.getTransactionHash());
        });
        refundRequestRepository.save(refundRequests);
    }
}
