package io.fundrequest.platform.admin.claim.service;

import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.fund.domain.RefundRequest;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.core.request.fund.infrastructure.RefundRequestRepository;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.request.infrastructure.azrael.RefundCommand;
import io.fundrequest.core.request.infrastructure.azrael.RefundTransaction;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.admin.refund.RefundModerationServiceImpl;
import io.fundrequest.platform.admin.service.ModerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.APPROVED;
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
    private RequestService requestService;

    @BeforeEach
    void setUp() {
        mappers = mock(Mappers.class);
        refundRequestRepository = mock(RefundRequestRepository.class);
        azraelClient = mock(AzraelClient.class);

        requestService = mock(RequestService.class);
        service = new RefundModerationServiceImpl(mappers, refundRequestRepository, azraelClient, requestService);
    }

    @Test
    void approve() {
        final long requestId = 654L;
        final long refundRequestId = 47658L;
        final String funderAddress = "0x03243eadsfs";
        final Platform platform = Platform.STACK_OVERFLOW;
        final String platformId = "gdfxfhcgjvtfrdasd";
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        requestDto.getIssueInformation().setPlatform(platform);
        requestDto.getIssueInformation().setPlatformId(platformId);
        final RefundRequest refundRequest = RefundRequest.builder()
                                                         .funderAddress(funderAddress)
                                                         .requestId(requestId)
                                                         .status(PENDING)
                                                         .build();
        final RefundCommand refundCommand = RefundCommand.builder()
                                                         .address(funderAddress)
                                                         .platform(platform.name())
                                                         .platformId(platformId)
                                                         .build();
        final String transactionHash = "0xghf32kjh";

        when(refundRequestRepository.findOne(refundRequestId)).thenReturn(Optional.of(refundRequest));
        when(requestService.findRequest(requestId)).thenReturn(requestDto);
        when(azraelClient.submitRefund(refundCommand)).thenReturn(RefundTransaction.builder().transactionHash(transactionHash).build());

        service.approve(refundRequestId);

        assertThat(refundRequest.getTransactionSubmitTime()).isEqualToIgnoringMinutes(LocalDateTime.now());
        assertThat(refundRequest.getStatus()).isEqualTo(APPROVED);
        assertThat(refundRequest.getTransactionHash()).isEqualTo(transactionHash);
        verify(refundRequestRepository).save(refundRequest);
    }

    @Test
    void listPending() {
        final List<RefundRequest> refundRequests = new ArrayList<>();
        final List<RefundRequestDto> expected = new ArrayList<>();

        when(refundRequestRepository.findAllByStatusIn(eq(Collections.singletonList(PENDING)), eq(new Sort("creationDate")))).thenReturn(refundRequests);
        when(mappers.mapList(eq(RefundRequest.class), eq(RefundRequestDto.class), same(refundRequests))).thenReturn(expected);

        final List<RefundRequestDto> result = service.listPending();

        assertThat(result).isSameAs(expected);
    }

    @Test
    void listFailed() {
        final List<RefundRequest> refundRequests = new ArrayList<>();
        final List<RefundRequestDto> expected = new ArrayList<>();

        when(refundRequestRepository.findAllByStatusIn(eq(Collections.singletonList(TRANSACTION_FAILED)), eq(new Sort("creationDate")))).thenReturn(refundRequests);
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