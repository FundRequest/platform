package io.fundrequest.core.request.claim;

import io.fundrequest.core.PrincipalMother;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.claim.domain.ClaimRequestStatus;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import io.fundrequest.core.request.claim.github.GithubClaimResolver;
import io.fundrequest.core.request.claim.infrastructure.RequestClaimRepository;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.security.Principal;
import java.util.Optional;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClaimServiceImplTest {

    private RequestRepository requestRepository;
    private RequestClaimRepository requestClaimRepository;
    private GithubClaimResolver githubClaimResolver;
    private Mappers mappers;
    private ClaimServiceImpl claimService;

    @Before
    public void setUp() {
        requestRepository = mock(RequestRepository.class);
        requestClaimRepository = mock(RequestClaimRepository.class);
        githubClaimResolver = mock(GithubClaimResolver.class);
        mappers = mock(Mappers.class);
        claimService = new ClaimServiceImpl(requestRepository, requestClaimRepository, githubClaimResolver, mappers);
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
                .thenReturn(solver.getName());
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
}
