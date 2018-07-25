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
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.APPROVED;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.PROCESSED;

@Service
public class RefundServiceImpl implements RefundService {

    private final RefundRequestRepository refundRequestRepository;
    private final RefundRepository refundRepository;
    private final RefundRequestDtoMapper refundRequestDtoMapper;
    private final CacheManager cacheManager;
    private final ApplicationEventPublisher applicationEventPublisher;

    public RefundServiceImpl(final RefundRequestRepository refundRequestRepository,
                             final RefundRepository refundRepository,
                             final RefundRequestDtoMapper refundRequestDtoMapper,
                             final CacheManager cacheManager,
                             final ApplicationEventPublisher applicationEventPublisher) {
        this.refundRequestRepository = refundRequestRepository;
        this.refundRepository = refundRepository;
        this.refundRequestDtoMapper = refundRequestDtoMapper;
        this.cacheManager = cacheManager;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    @Transactional
    public void requestRefund(final RequestRefundCommand requestRefundCommand) {
        refundRequestRepository.save(RefundRequest.builder()
                                                  .requestId(requestRefundCommand.getRequestId())
                                                  .funderAddress(requestRefundCommand.getFunderAddress())
                                                  .requestedBy(requestRefundCommand.getRequestedBy())
                                                  .build());
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundRequestDto> findAllRefundRequestsFor(final long requestId, final RefundRequestStatus... statuses) {
        return refundRequestDtoMapper.mapToList(refundRequestRepository.findAllByRequestIdAndStatusIn(requestId, statuses));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundRequestDto> findAllRefundRequestsFor(final long requestId, final String funderAddress, final RefundRequestStatus status) {
        return refundRequestDtoMapper.mapToList(refundRequestRepository.findAllByRequestIdAndFunderAddressAndStatus(requestId, funderAddress, status));
    }

    @Override
    @Transactional
    public void refundProcessed(final RefundProcessedCommand command) {
        final List<RefundRequest> refundRequests = refundRequestRepository.findAllByRequestIdAndStatusIn(command.getRequestId(), APPROVED);
        refundRequests.forEach(refundRequest -> {
            refundRequest.setStatus(PROCESSED);
            refundRequest.setTransactionHash(command.getTransactionHash());
        });
        refundRequestRepository.save(refundRequests);

        final Refund refund = Refund.builder()
                                    .requestId(command.getRequestId())
                                    .funderAddress(command.getFunderAddress())
                                    .blockchainEventId(command.getBlockchainEventId())
                                    .requestedBy(resolveRequestedBy(command, refundRequests).orElse(null))
                                    .tokenValue(TokenValue.builder()
                                                          .amountInWei(new BigDecimal(command.getAmount()))
                                                          .tokenAddress(command.getTokenHash())
                                                          .build())
                                    .build();
        refundRepository.save(refund);
        cacheManager.getCache ("funds").evict(command.getRequestId());
        applicationEventPublisher.publishEvent(new RefundProcessedEvent(refund));
    }

    private Optional<String> resolveRequestedBy(final RefundProcessedCommand command, final List<RefundRequest> refundRequests) {
        if (refundRequests.isEmpty()) {
            return refundRequestRepository.findByTransactionHash(command.getTransactionHash()).map(RefundRequest::getRequestedBy);
        } else {
            return refundRequests.stream()
                                 .map(RefundRequest::getRequestedBy)
                                 .filter(Objects::nonNull)
                                 .reduce((s, s2) -> s);
        }
    }
}
