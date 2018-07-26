package io.fundrequest.platform.admin.infrastructure;

import io.fundrequest.core.request.claim.domain.ClaimRequestStatus;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import io.fundrequest.core.request.claim.infrastructure.RequestClaimRepository;
import io.fundrequest.core.request.fund.domain.RefundRequest;
import io.fundrequest.core.request.fund.domain.RefundRequestStatus;
import io.fundrequest.core.request.fund.infrastructure.RefundRequestRepository;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.transactions.TransactionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

class RequestVacuumerTest {

    private RequestVacuumer requestVacuumer;

    private RequestClaimRepository requestClaimRepository;
    private RefundRequestRepository refundRequestRepository;
    private AzraelClient azraelClient;

    @BeforeEach
    public void setUp() {
        requestClaimRepository = mock(RequestClaimRepository.class);
        refundRequestRepository = mock(RefundRequestRepository.class);
        azraelClient = mock(AzraelClient.class);

        requestVacuumer = new RequestVacuumer(requestClaimRepository, refundRequestRepository, azraelClient);
    }

    @Test
    void cleanClaims() {
        final RequestClaim requestClaim1 = RequestClaim.builder().status(ClaimRequestStatus.APPROVED).transactionHash("0x213").build();
        final RequestClaim requestClaim2 = RequestClaim.builder().status(ClaimRequestStatus.APPROVED).transactionHash("0x3243").build();
        final RequestClaim requestClaim3 = RequestClaim.builder().status(ClaimRequestStatus.APPROVED).transactionHash(null).build();
        final RequestClaim requestClaim4 = RequestClaim.builder().status(ClaimRequestStatus.APPROVED).transactionHash("0x687").build();

        when(requestClaimRepository.findByStatus(ClaimRequestStatus.APPROVED)).thenReturn(Arrays.asList(requestClaim1, requestClaim2, requestClaim3, requestClaim4));
        when(azraelClient.getTransactionStatus(requestClaim1.getTransactionHash())).thenReturn(TransactionStatus.SUCCEEDED);
        when(azraelClient.getTransactionStatus(requestClaim2.getTransactionHash())).thenReturn(TransactionStatus.NOT_FOUND);
        when(azraelClient.getTransactionStatus(requestClaim4.getTransactionHash())).thenReturn(TransactionStatus.FAILED);

        requestVacuumer.cleanClaims();

        assertThat(requestClaim1.getStatus()).isEqualTo(ClaimRequestStatus.APPROVED);
        assertThat(requestClaim2.getStatus()).isEqualTo(ClaimRequestStatus.APPROVED);
        assertThat(requestClaim3.getStatus()).isEqualTo(ClaimRequestStatus.APPROVED);
        assertThat(requestClaim4.getStatus()).isEqualTo(ClaimRequestStatus.TRANSACTION_FAILED);
        verify(requestClaimRepository).findByStatus(ClaimRequestStatus.APPROVED);
        verify(requestClaimRepository).save(requestClaim4);
        verifyNoMoreInteractions(requestClaimRepository);
    }

    @Test
    void cleanRefunds() {
        final RefundRequest refundRequest1 = RefundRequest.builder().status(RefundRequestStatus.APPROVED).transactionHash("0x213").build();
        final RefundRequest refundRequest2 = RefundRequest.builder().status(RefundRequestStatus.APPROVED).transactionHash("0x3243").build();
        final RefundRequest refundRequest3 = RefundRequest.builder().status(RefundRequestStatus.APPROVED).transactionHash(null).build();
        final RefundRequest refundRequest4 = RefundRequest.builder().status(RefundRequestStatus.APPROVED).transactionHash("0x687").build();

        when(refundRequestRepository.findAllByStatus(RefundRequestStatus.APPROVED)).thenReturn(Arrays.asList(refundRequest1, refundRequest2, refundRequest3, refundRequest4));
        when(azraelClient.getTransactionStatus(refundRequest1.getTransactionHash())).thenReturn(TransactionStatus.SUCCEEDED);
        when(azraelClient.getTransactionStatus(refundRequest2.getTransactionHash())).thenReturn(TransactionStatus.NOT_FOUND);
        when(azraelClient.getTransactionStatus(refundRequest4.getTransactionHash())).thenReturn(TransactionStatus.FAILED);

        requestVacuumer.cleanRefunds();

        assertThat(refundRequest1.getStatus()).isEqualTo(RefundRequestStatus.APPROVED);
        assertThat(refundRequest2.getStatus()).isEqualTo(RefundRequestStatus.APPROVED);
        assertThat(refundRequest3.getStatus()).isEqualTo(RefundRequestStatus.APPROVED);
        assertThat(refundRequest4.getStatus()).isEqualTo(RefundRequestStatus.TRANSACTION_FAILED);
        verify(refundRequestRepository).findAllByStatus(RefundRequestStatus.APPROVED);
        verify(refundRequestRepository).save(refundRequest4);
        verifyNoMoreInteractions(refundRequestRepository);
    }
}