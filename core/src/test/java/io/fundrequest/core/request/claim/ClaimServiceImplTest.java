package io.fundrequest.core.request.claim;

import io.fundrequest.core.PrincipalMother;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.claim.domain.ClaimRequestStatus;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import io.fundrequest.core.request.claim.domain.RequestClaimMother;
import io.fundrequest.core.request.claim.github.GithubClaimResolver;
import io.fundrequest.core.request.claim.infrastructure.RequestClaimRepository;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.request.infrastructure.azrael.ClaimSignature;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.security.Principal;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClaimServiceImplTest {

    private RequestRepository requestRepository;
    private RequestClaimRepository requestClaimRepository;
    private GithubClaimResolver githubClaimResolver;
    private Mappers mappers;
    private RabbitTemplate rabbitTemplate;
    private AzraelClient azraelClient;
    private ClaimServiceImpl claimService;

    @Before
    public void setUp() throws Exception {
        requestRepository = mock(RequestRepository.class);
        requestClaimRepository = mock(RequestClaimRepository.class);
        githubClaimResolver = mock(GithubClaimResolver.class);
        mappers = mock(Mappers.class);
        rabbitTemplate = mock(RabbitTemplate.class);
        azraelClient = mock(AzraelClient.class);
        claimService = new ClaimServiceImpl(requestRepository, requestClaimRepository, githubClaimResolver, mappers, rabbitTemplate, azraelClient);
    }

    @Test
    public void claim() {
        Principal solver = PrincipalMother.davyvanroy();
        UserClaimRequest userClaimRequest = UserClaimRequestMother.kazuki43zooApiStub().build();
        Request request = RequestMother.freeCodeCampNoUserStories().build();
        when(requestRepository.findByPlatformAndPlatformId(userClaimRequest.getPlatform(), userClaimRequest.getPlatformId()))
                .thenReturn(Optional.of(request));
        RequestDto requestDto = RequestDtoMother.freeCodeCampNoUserStories();
        when(mappers.map(Request.class, RequestDto.class, request)).thenReturn(requestDto);
        when(githubClaimResolver.getUserPlatformUsername(solver, request.getIssueInformation().getPlatform()))
        .thenReturn(Optional.of(solver.getName()));
        when(githubClaimResolver.canClaim(solver, requestDto)).thenReturn(true);

        claimService.claim(solver, userClaimRequest);

        assertThat(request.getStatus()).isEqualTo(RequestStatus.CLAIM_REQUESTED);
        verify(requestRepository).save(request);

        ArgumentCaptor<RequestClaim> requestClaimArgumentCaptor = ArgumentCaptor.forClass(RequestClaim.class);
        verify(requestClaimRepository).save(requestClaimArgumentCaptor.capture());
        RequestClaim requestClaim = requestClaimArgumentCaptor.getValue();
        assertThat(requestClaim.getRequestId()).isEqualTo(request.getId());
        assertThat(requestClaim.getAddress()).isEqualTo(userClaimRequest.getAddress());
        assertThat(requestClaim.getSolver()).isEqualTo(solver.getName());
        assertThat(requestClaim.getFlagged()).isFalse();
        assertThat(requestClaim.getStatus()).isEqualTo(ClaimRequestStatus.PENDING);
    }

    @Test
    public void approveClaim() {
        RequestClaim requestClaim = RequestClaimMother.requestClaim().build();
        Request request = RequestMother.freeCodeCampNoUserStories().build();
        when(requestRepository.findOne(1L)).thenReturn(Optional.of(request));
        ClaimSignature sig = new ClaimSignature();
        when(azraelClient.getSignature(any())).thenReturn(sig);
        when(requestClaimRepository.findOne(requestClaim.getId())).thenReturn(Optional.of(requestClaim));

        claimService.approveClaim(requestClaim.getId());

        assertThat(request.getStatus()).isEqualTo(RequestStatus.CLAIM_APPROVED);
        assertThat(requestClaim.getStatus()).isEqualTo(ClaimRequestStatus.APPROVED);
        verify(requestClaimRepository).save(requestClaim);
        verify(requestRepository).save(request);

        verify(rabbitTemplate).convertAndSend(sig);
    }

    @Test
    public void declineClaim() {
        RequestClaim requestClaim = RequestClaimMother.requestClaim().build();
        Request request = RequestMother.freeCodeCampNoUserStories().build();
        when(requestRepository.findOne(1L)).thenReturn(Optional.of(request));
        when(requestClaimRepository.findOne(requestClaim.getId())).thenReturn(Optional.of(requestClaim));

        claimService.declineClaim(requestClaim.getId());

        assertThat(request.getStatus()).isEqualTo(RequestStatus.FUNDED);
        assertThat(requestClaim.getStatus()).isEqualTo(ClaimRequestStatus.DECLINED);
        verify(requestClaimRepository).save(requestClaim);
        verify(requestRepository).save(request);

    }
}