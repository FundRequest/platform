package io.fundrequest.platform.admin.claim.service;

import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.fund.domain.RefundRequest;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.core.request.fund.infrastructure.RefundRequestRepository;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.platform.admin.refund.RefundModerationServiceImpl;
import io.fundrequest.platform.admin.service.ModerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.DECLINED;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.PENDING;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.TRANSACTION_FAILED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RefundModerationServiceImplTest {

    private ModerationService<RefundRequestDto> service;

    private Mappers mappers;
    private RefundRequestRepository refundRequestRepository;
    private AzraelClient azraelClient;

    @BeforeEach
    void setUp() {
        mappers = mock(Mappers.class);
        refundRequestRepository = mock(RefundRequestRepository.class);
        azraelClient = mock(AzraelClient.class);

        service = new RefundModerationServiceImpl(mappers, refundRequestRepository, azraelClient);
    }

    @Test
    void approve() {
        final long refundRequestId = 47658L;

        when(refundRequestRepository.findOne(refundRequestId)).thenReturn(Optional.of(RefundRequest.builder()
                                                                                                   .status(PENDING)
                                                                                                   .build()));

        service.approve(refundRequestId);

    }

    @Test
    void listPending() {
        final List<RefundRequest> refundRequests = new ArrayList<>();
        final List<RefundRequestDto> expected = new ArrayList<>();

        when(refundRequestRepository.findByStatusIn(eq(Collections.singletonList(PENDING)), eq(new Sort("creationDate")))).thenReturn(refundRequests);
        when(mappers.mapList(eq(RefundRequest.class), eq(RefundRequestDto.class), same(refundRequests))).thenReturn(expected);

        final List<RefundRequestDto> result = service.listPending();

        assertThat(result).isSameAs(expected);
    }

    @Test
    void listFailed() {
        final List<RefundRequest> refundRequests = new ArrayList<>();
        final List<RefundRequestDto> expected = new ArrayList<>();

        when(refundRequestRepository.findByStatusIn(eq(Collections.singletonList(TRANSACTION_FAILED)), eq(new Sort("creationDate")))).thenReturn(refundRequests);
        when(mappers.mapList(eq(RefundRequest.class), eq(RefundRequestDto.class), same(refundRequests))).thenReturn(expected);

        final List<RefundRequestDto> result = service.listFailed();

        assertThat(result).isSameAs(expected);
    }

    @Test
    void decline() {
        final long refundRequestId = 58L;
        final RefundRequest refundRequest = RefundRequest.builder()
                                                         .status(PENDING)
                                                         .build();

        when(refundRequestRepository.findOne(refundRequestId)).thenReturn(Optional.of(refundRequest));

        service.decline(refundRequestId);

        assertThat(refundRequest.getStatus()).isEqualTo(DECLINED);
        verify(refundRequestRepository).save(refundRequest);
    }
}