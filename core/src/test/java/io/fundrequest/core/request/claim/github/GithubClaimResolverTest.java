package io.fundrequest.core.request.claim.github;

import io.fundrequest.core.PrincipalMother;
import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.claim.UserClaimRequest;
import io.fundrequest.core.request.claim.dto.UserClaimableDto;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.request.infrastructure.azrael.ClaimSignature;
import io.fundrequest.core.request.infrastructure.azrael.SignClaimCommand;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.keycloak.UserIdentity;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GithubClaimResolverTest {

    private GithubClaimResolver claimResolver;
    private GithubSolverResolver githubSolverResolver;
    private AzraelClient azraelClient;
    private KeycloakRepository keycloakRepository;

    @Before
    public void setUp() throws Exception {
        githubSolverResolver = mock(GithubSolverResolver.class);
        azraelClient = mock(AzraelClient.class);
        keycloakRepository = mock(KeycloakRepository.class);
        claimResolver = new GithubClaimResolver(githubSolverResolver, azraelClient, keycloakRepository);
    }

    @Test
    public void userClaimableResult() {
        Principal principal = PrincipalMother.davyvanroy();
        RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        when(githubSolverResolver.solveResolver(requestDto)).thenReturn(Optional.of("davyvanroy"));
        when(keycloakRepository.getUserIdentities(principal.getName()))
                .thenReturn(Stream.of(UserIdentity.builder().provider(Provider.GITHUB).username("davyvanroy").build()));

        UserClaimableDto result = claimResolver.userClaimableResult(principal, requestDto);

        assertThat(result.isClaimable()).isTrue();
        assertThat(result.isClaimableByUser()).isTrue();
    }

    @Test
    public void userClaimableResultClaimRequested() {
        Principal principal = PrincipalMother.davyvanroy();
        RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        requestDto.setStatus(RequestStatus.CLAIM_REQUESTED);
        when(githubSolverResolver.solveResolver(requestDto)).thenReturn(Optional.of("davyvanroy"));
        when(keycloakRepository.getUserIdentities(principal.getName()))
                .thenReturn(Stream.of(UserIdentity.builder().provider(Provider.GITHUB).username("davyvanroy").build()));

        UserClaimableDto result = claimResolver.userClaimableResult(principal, requestDto);

        assertThat(result.isClaimable()).isFalse();
        assertThat(result.isClaimableByUser()).isFalse();
    }

    @Test
    public void canClaim() {
        Principal principal = PrincipalMother.davyvanroy();
        RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        when(githubSolverResolver.solveResolver(requestDto)).thenReturn(Optional.of("davyvanroy"));
        when(keycloakRepository.getUserIdentities(principal.getName()))
                .thenReturn(Stream.of(UserIdentity.builder().provider(Provider.GITHUB).username("davyvanroy").build()));

        assertThat(claimResolver.canClaim(principal, requestDto)).isTrue();

    }

    @Test
    public void claim() throws IOException {


        RequestDto request = RequestDtoMother.freeCodeCampNoUserStories();
        UserClaimRequest userClaimRequest = createClaimRequest(request);

        when(keycloakRepository.getUserIdentities("davyvanroy")).thenReturn(Stream.of(UserIdentity.builder()
                                                                                                  .provider(Provider.GITHUB)
                                                                                                  .username("davyvanroy")
                                                                                                  .build()));
        when(githubSolverResolver.solveResolver(request)).thenReturn(Optional.of("davyvanroy"));
        SignClaimCommand signClaimCommand = createSignClaimCommand(userClaimRequest, "davyvanroy");
        ClaimSignature claimSignature = createClaimSignature(signClaimCommand);
        when(azraelClient.getSignature(signClaimCommand)).thenReturn(claimSignature);

        SignedClaim result = claimResolver.getSignedClaim(() -> "davyvanroy", userClaimRequest, request);

        assertThat(result.getPlatform().toString()).isEqualTo(claimSignature.getPlatform());
        assertThat(result.getPlatformId()).isEqualTo(claimSignature.getPlatformId());
        assertThat(result.getSolver()).isEqualTo(claimSignature.getSolver());
        assertThat(result.getSolverAddress()).isEqualTo(claimSignature.getAddress());
        assertThat(result.getR()).isEqualTo(claimSignature.getR());
        assertThat(result.getS()).isEqualTo(claimSignature.getS());
        assertThat(result.getV()).isEqualTo(claimSignature.getV());
    }

    private ClaimSignature createClaimSignature(SignClaimCommand claimCommand) {
        ClaimSignature sig = new ClaimSignature();
        sig.setPlatform(claimCommand.getPlatform());
        sig.setPlatformId(claimCommand.getPlatformId());
        sig.setSolver(claimCommand.getSolver());
        sig.setAddress(claimCommand.getAddress());
        sig.setR("r");
        sig.setS("s");
        sig.setV(1);
        return sig;
    }

    private UserClaimRequest createClaimRequest(RequestDto request) {
        return UserClaimRequest.builder()
                               .address("0x0")
                               .platformId(request.getIssueInformation().getPlatformId())
                               .platform(request.getIssueInformation().getPlatform())
                               .build();
    }

    private SignClaimCommand createSignClaimCommand(UserClaimRequest userClaimRequest, String solver) {
        return SignClaimCommand.builder()
                               .platform(userClaimRequest.getPlatform().toString())
                               .platformId(userClaimRequest.getPlatformId())
                               .address(userClaimRequest.getAddress())
                               .solver(solver)
                               .build();
    }
}