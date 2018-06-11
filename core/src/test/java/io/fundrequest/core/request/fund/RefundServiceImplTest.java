package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.command.RequestRefundCommand;
import io.fundrequest.core.request.fund.domain.RefundRequest;
import io.fundrequest.core.request.fund.domain.RefundRequestStatus;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.core.request.fund.dto.RefundRequestDtoMapper;
import io.fundrequest.core.request.fund.infrastructure.RefundRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.PENDING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.refEq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RefundServiceImplTest {

    private RefundService refundService;

    private RefundRequestRepository refundRequestRepository;
    private RefundRequestDtoMapper refundRequestDtoMapper;

    @BeforeEach
    void setUp() {
        refundRequestRepository = mock(RefundRequestRepository.class);
        refundRequestDtoMapper = mock(RefundRequestDtoMapper.class);
        refundService = new RefundServiceImpl(refundRequestRepository, refundRequestDtoMapper);
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
    public void findAllRefundRequestsFor() {
        final long requestId = 3389L;
        final RefundRequestStatus status = PENDING;
        final List<RefundRequest> refundRequests = new ArrayList<>();
        final ArrayList<RefundRequestDto> refundRequestDtos = new ArrayList<>();

        when(refundRequestRepository.findAllByRequestIdAndStatus(requestId, status)).thenReturn(refundRequests);
        when(refundRequestDtoMapper.mapToList(same(refundRequests))).thenReturn(refundRequestDtos);

        final List<RefundRequestDto> result = refundService.findAllRefundRequestsFor(requestId, status);

        assertThat(result).isSameAs(refundRequestDtos);
    }
}
