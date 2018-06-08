package io.fundrequest.platform.admin.claim;


import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.claim.domain.ClaimRequestStatus;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import io.fundrequest.core.request.claim.domain.RequestClaimMother;
import io.fundrequest.core.request.claim.infrastructure.RequestClaimRepository;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.request.infrastructure.azrael.ClaimSignature;
import io.fundrequest.core.request.infrastructure.azrael.ClaimTransaction;
import io.fundrequest.platform.admin.claim.service.ClaimModerationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClaimModerationServiceImplTest {

    private ClaimModerationServiceImpl claimModerationService;
    private RequestRepository requestRepository;
    private RequestClaimRepository requestClaimRepository;
    private RabbitTemplate rabbitTemplate;
    private AzraelClient azraelClient;

    @Before
    public void setUp() {
        requestRepository = mock(RequestRepository.class);
        requestClaimRepository = mock(RequestClaimRepository.class);
        final Mappers mappers = mock(Mappers.class);
        rabbitTemplate = mock(RabbitTemplate.class);
        azraelClient = mock(AzraelClient.class);
        claimModerationService = new ClaimModerationServiceImpl(mappers, requestClaimRepository, requestRepository, azraelClient);
    }


    @Test
    public void approveClaim() {
        RequestClaim requestClaim = RequestClaimMother.requestClaim().build();
        Request request = RequestMother.freeCodeCampNoUserStories().build();
        when(requestRepository.findOne(1L)).thenReturn(Optional.of(request));
        ClaimSignature sig = new ClaimSignature();
        when(azraelClient.getSignature(any())).thenReturn(sig);
        when(requestClaimRepository.findOne(requestClaim.getId())).thenReturn(Optional.of(requestClaim));
        when(azraelClient.submitClaim(sig)).thenReturn(ClaimTransaction.builder().transactionHash("0x1").build());

        claimModerationService.approve(requestClaim.getId());

        assertThat(request.getStatus()).isEqualTo(RequestStatus.CLAIM_APPROVED);
        assertThat(requestClaim.getStatus()).isEqualTo(ClaimRequestStatus.APPROVED);
        assertThat(requestClaim.getTransactionHash()).isEqualTo("0x1");
        verify(requestClaimRepository).save(requestClaim);
        verify(requestRepository).save(request);
    }

    @Test
    public void declineClaim() {
        RequestClaim requestClaim = RequestClaimMother.requestClaim().build();
        Request request = RequestMother.freeCodeCampNoUserStories().build();
        when(requestRepository.findOne(1L)).thenReturn(Optional.of(request));
        when(requestClaimRepository.findOne(requestClaim.getId())).thenReturn(Optional.of(requestClaim));

        claimModerationService.decline(requestClaim.getId());

        assertThat(request.getStatus()).isEqualTo(RequestStatus.FUNDED);
        assertThat(requestClaim.getStatus()).isEqualTo(ClaimRequestStatus.DECLINED);
        verify(requestClaimRepository).save(requestClaim);
        verify(requestRepository).save(request);

    }
}