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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.APPROVED;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.PENDING;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.PROCESSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.refEq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RefundServiceImplTest {

    private RefundService refundService;

    private RefundRequestRepository refundRequestRepository;
    private RefundRepository refundRepository;
    private RefundRequestDtoMapper refundRequestDtoMapper;

    @BeforeEach
    void setUp() {
        refundRequestRepository = mock(RefundRequestRepository.class);
        refundRepository = mock(RefundRepository.class);
        refundRequestDtoMapper = mock(RefundRequestDtoMapper.class);
        refundService = new RefundServiceImpl(refundRequestRepository, refundRepository, refundRequestDtoMapper);
    }

    @Test
    void requestRefund() {
        final long requestId = 547L;
        final String funderAddress = "hjfgkh";

        refundService.requestRefund(RequestRefundCommand.builder().requestId(requestId).funderAddress(funderAddress).build());

        verify(refundRequestRepository).save(refEq(RefundRequest.builder()
                                                                .requestId(requestId)
                                                                .funderAddress(funderAddress)
                                                                .build()));
    }

    @Test
    public void findAllRefundRequestsForRequestIdAndStatus() {
        final long requestId = 3389L;
        final RefundRequestStatus status = PENDING;
        final List<RefundRequest> refundRequests = new ArrayList<>();
        final ArrayList<RefundRequestDto> refundRequestDtos = new ArrayList<>();

        when(refundRequestRepository.findAllByRequestIdAndStatus(requestId, status)).thenReturn(refundRequests);
        when(refundRequestDtoMapper.mapToList(same(refundRequests))).thenReturn(refundRequestDtos);

        final List<RefundRequestDto> result = refundService.findAllRefundRequestsFor(requestId, status);

        assertThat(result).isSameAs(refundRequestDtos);
    }

    @Test
    public void findAllRefundRequestsForRequestIdAndFunderAddressAndStatus() {
        final long requestId = 347L;
        final String funderAddress = "0x6457hfd";
        final RefundRequestStatus status = PROCESSED;
        final List<RefundRequest> refundRequests = new ArrayList<>();
        final ArrayList<RefundRequestDto> refundRequestDtos = new ArrayList<>();

        when(refundRequestRepository.findAllByRequestIdAndFunderAddressAndStatus(requestId, funderAddress, status)).thenReturn(refundRequests);
        when(refundRequestDtoMapper.mapToList(same(refundRequests))).thenReturn(refundRequestDtos);

        final List<RefundRequestDto> result = refundService.findAllRefundRequestsFor(requestId, funderAddress, status);

        assertThat(result).isSameAs(refundRequestDtos);
    }

    @Test
    public void refundProcessed() {
        final long requestId = 644L;
        final String amount = "4530000000000000000";
        final String tokenHash = "0x05466";
        final String funderAddress = "0x67879809";
        final long blockchainEventId = 34L;
        final String transactionHash = "0x46578";
        final RefundRequest refundRequest1 = mock(RefundRequest.class);
        final RefundRequest refundRequest2 = mock(RefundRequest.class);
        final List<RefundRequest> refundRequests = Arrays.asList(refundRequest1, refundRequest2);

        when(refundRequestRepository.findAllByRequestIdAndStatus(requestId, APPROVED)).thenReturn(refundRequests);

        refundService.refundProcessed(RefundProcessedCommand.builder()
                                                            .requestId(requestId)
                                                            .amount(amount)
                                                            .tokenHash(tokenHash)
                                                            .funderAddress(funderAddress)
                                                            .blockchainEventId(blockchainEventId)
                                                            .transactionHash(transactionHash)
                                                            .build());

        verify(refundRepository).save(Refund.builder()
                                            .requestId(requestId)
                                            .tokenValue(TokenValue.builder()
                                                                  .amountInWei(new BigDecimal(amount))
                                                                  .tokenAddress(tokenHash)
                                                                  .build())
                                            .funderAddress(funderAddress)
                                            .blockchainEventId(blockchainEventId)
                                            .build());
        verify(refundRequest1).setTransactionHash(transactionHash);
        verify(refundRequest1).setStatus(PROCESSED);
        verify(refundRequest2).setTransactionHash(transactionHash);
        verify(refundRequest2).setStatus(PROCESSED);
        verify(refundRequestRepository).save(refundRequests);
    }
}
