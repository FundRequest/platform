package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.command.RequestRefundCommand;
import io.fundrequest.core.request.fund.domain.RefundRequest;
import io.fundrequest.core.request.fund.infrastructure.RefundRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class RefundServiceImplTest {

    private RefundService refundService;

    private RefundRequestRepository refundRequestRepository;

    @BeforeEach
    void setUp() {
        refundRequestRepository = mock(RefundRequestRepository.class);
        refundService = new RefundServiceImpl(refundRequestRepository);
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
}