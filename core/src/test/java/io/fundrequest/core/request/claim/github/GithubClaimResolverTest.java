package io.fundrequest.core.request.claim.github;

import io.fundrequest.core.keycloak.KeycloakRepository;
import io.fundrequest.core.keycloak.UserIdentity;
import io.fundrequest.core.request.claim.SignClaimRequest;
import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.request.infrastructure.azrael.ClaimSignature;
import io.fundrequest.core.request.infrastructure.azrael.SignClaimCommand;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
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
    }

    @Test
    public void claim() throws IOException {
        claimResolver = new GithubClaimResolver(githubSolverResolver, azraelClient, keycloakRepository);

        RequestDto request = RequestDtoMother.freeCodeCampNoUserStories();
        SignClaimRequest signClaimRequest = createClaimRequest(request);

        when(keycloakRepository.getUserIdentities("davyvanroy")).thenReturn(Stream.of(new UserIdentity("github", "davyvanroy")));
        when(githubSolverResolver.solveResolver(request)).thenReturn(Optional.of("davyvanroy"));
        SignClaimCommand signClaimCommand = createSignClaimCommand(signClaimRequest, "davyvanroy");
        ClaimSignature claimSignature = createClaimSignature(signClaimCommand);
        when(azraelClient.getSignature(signClaimCommand)).thenReturn(claimSignature);

        SignedClaim result = claimResolver.getSignedClaim(() -> "davyvanroy", signClaimRequest, request);

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

    private SignClaimRequest createClaimRequest(RequestDto request) {
        SignClaimRequest signClaimRequest = new SignClaimRequest();
        signClaimRequest.setAddress("0x0");
        signClaimRequest.setPlatform(request.getIssueInformation().getPlatform());
        signClaimRequest.setPlatformId(request.getIssueInformation().getPlatformId());
        return signClaimRequest;
    }

    private SignClaimCommand createSignClaimCommand(SignClaimRequest signClaimRequest, String solver) {
        SignClaimCommand signClaimCommand = new SignClaimCommand();
        signClaimCommand.setPlatform(signClaimRequest.getPlatform().toString());
        signClaimCommand.setPlatformId(signClaimRequest.getPlatformId());
        signClaimCommand.setAddress(signClaimRequest.getAddress());
        signClaimCommand.setSolver(solver);
        return signClaimCommand;
    }
}