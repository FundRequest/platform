package io.fundrequest.core.request.claim.github;

import io.fundrequest.core.PrincipalMother;
import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.claim.UserClaimRequest;
import io.fundrequest.core.request.claim.dto.UserClaimableDto;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.request.infrastructure.azrael.ClaimSignature;
import io.fundrequest.core.request.infrastructure.azrael.SignClaimCommand;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.github.GithubSolverResolver;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.keycloak.UserIdentity;
import org.junit.Before;
import org.junit.Test;

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
        final Principal principal = PrincipalMother.davyvanroy();
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        requestDto.setStatus(RequestStatus.FUNDED);
        final IssueInformationDto issueInformation = requestDto.getIssueInformation();

        when(githubSolverResolver.resolveSolver(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(Optional.of("davyvanroy"));
        when(keycloakRepository.getUserIdentities(principal.getName())).thenReturn(Stream.of(UserIdentity.builder().provider(Provider.GITHUB).username("davyvanroy").build()));

        final UserClaimableDto result = claimResolver.userClaimableResult(principal, requestDto);

        assertThat(result.isClaimable()).isTrue();
        assertThat(result.isClaimableByUser()).isTrue();
    }

    @Test
    public void userClaimableResultNoPrincipal() {
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        requestDto.setStatus(RequestStatus.FUNDED);
        final IssueInformationDto issueInformation = requestDto.getIssueInformation();

        when(githubSolverResolver.resolveSolver(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(Optional.of("davyvanroy"));

        final UserClaimableDto result = claimResolver.userClaimableResult(null, requestDto);

        assertThat(result.isClaimable()).isTrue();
        assertThat(result.isClaimableByUser()).isFalse();
    }

    @Test
    public void userClaimableResultClaimRequested() {
        final Principal principal = PrincipalMother.davyvanroy();
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        requestDto.setStatus(RequestStatus.CLAIM_REQUESTED);
        final IssueInformationDto issueInformation = requestDto.getIssueInformation();

        when(githubSolverResolver.resolveSolver(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(Optional.of("davyvanroy"));
        when(keycloakRepository.getUserIdentities(principal.getName())).thenReturn(Stream.of(UserIdentity.builder().provider(Provider.GITHUB).username("davyvanroy").build()));

        final UserClaimableDto result = claimResolver.userClaimableResult(principal, requestDto);

        assertThat(result.isClaimable()).isFalse();
        assertThat(result.isClaimableByUser()).isFalse();
    }

    @Test
    public void canClaim() {
        final Principal principal = PrincipalMother.davyvanroy();
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        final IssueInformationDto issueInformation = requestDto.getIssueInformation();

        when(githubSolverResolver.resolveSolver(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(Optional.of("davyvanroy"));
        when(keycloakRepository.getUserIdentities(principal.getName())).thenReturn(Stream.of(UserIdentity.builder().provider(Provider.GITHUB).username("davyvanroy").build()));

        assertThat(claimResolver.canClaim(principal, requestDto)).isTrue();
    }

    @Test
    public void claim() {
        final RequestDto request = RequestDtoMother.freeCodeCampNoUserStories();
        final UserClaimRequest userClaimRequest = createClaimRequest(request);
        final IssueInformationDto issueInformation = request.getIssueInformation();
        final SignClaimCommand signClaimCommand = createSignClaimCommand(userClaimRequest, "davyvanroy");
        final ClaimSignature claimSignature = createClaimSignature(signClaimCommand);

        when(keycloakRepository.getUserIdentities("davyvanroy")).thenReturn(Stream.of(UserIdentity.builder()
                                                                                                  .provider(Provider.GITHUB)
                                                                                                  .username("davyvanroy")
                                                                                                  .build()));
        when(githubSolverResolver.resolveSolver(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(Optional.of("davyvanroy"));
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

    private ClaimSignature createClaimSignature(final SignClaimCommand claimCommand) {
        final ClaimSignature sig = new ClaimSignature();
        sig.setPlatform(claimCommand.getPlatform());
        sig.setPlatformId(claimCommand.getPlatformId());
        sig.setSolver(claimCommand.getSolver());
        sig.setAddress(claimCommand.getAddress());
        sig.setR("r");
        sig.setS("s");
        sig.setV(1);
        return sig;
    }

    private UserClaimRequest createClaimRequest(final RequestDto request) {
        return UserClaimRequest.builder()
                               .address("0x0")
                               .platformId(request.getIssueInformation().getPlatformId())
                               .platform(request.getIssueInformation().getPlatform())
                               .build();
    }

    private SignClaimCommand createSignClaimCommand(final UserClaimRequest userClaimRequest, final String solver) {
        return SignClaimCommand.builder()
                               .platform(userClaimRequest.getPlatform().toString())
                               .platformId(userClaimRequest.getPlatformId())
                               .address(userClaimRequest.getAddress())
                               .solver(solver)
                               .build();
    }
}