package io.fundrequest.core.request.claim.github;

import io.fundrequest.core.keycloak.KeycloakRepository;
import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.claim.UserClaimRequest;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.request.infrastructure.azrael.ClaimSignature;
import io.fundrequest.core.request.infrastructure.azrael.SignClaimCommand;
import io.fundrequest.core.request.view.RequestDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;

@Component
public class GithubClaimResolver {

    private GithubSolverResolver githubSolverResolver;
    private AzraelClient azraelClient;
    private KeycloakRepository keycloakRepository;

    public GithubClaimResolver(GithubSolverResolver githubSolverResolver, AzraelClient azraelClient, KeycloakRepository keycloakRepository) {
        this.githubSolverResolver = githubSolverResolver;
        this.azraelClient = azraelClient;
        this.keycloakRepository = keycloakRepository;
    }

    public SignedClaim getSignedClaim(Principal user, UserClaimRequest userClaimRequest, RequestDto request) {
        try {
            final String solver = getSolver(user, userClaimRequest, request);
            final ClaimSignature signature = getSignature(userClaimRequest, solver);
            return new SignedClaim(
                    solver,
                    signature.getAddress(),
                    userClaimRequest.getPlatform(),
                    signature.getPlatformId(),
                    signature.getR(),
                    signature.getS(),
                    signature.getV());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Boolean canClaim(Principal user, RequestDto request) {
        Optional<String> solver = githubSolverResolver.solveResolver(request);
        return solver.isPresent()
                && solver.get().equalsIgnoreCase(getUserPlatformUsername(user, request.getIssueInformation().getPlatform()));
    }

    private String getSolver(Principal user, UserClaimRequest userClaimRequest, RequestDto request) throws IOException {
        String solver = githubSolverResolver.solveResolver(request).orElseThrow(() -> new RuntimeException("Unable to get solver"));
        if (!solver.equalsIgnoreCase(getUserPlatformUsername(user, userClaimRequest.getPlatform()))) {
            throw new RuntimeException("Claim executed by wrong user");
        }
        return solver;
    }

    private ClaimSignature getSignature(UserClaimRequest userClaimRequest, String solver) {
        SignClaimCommand command = SignClaimCommand.builder()
                .platform(userClaimRequest.getPlatform().toString())
                .platformId(userClaimRequest.getPlatformId())
                .solver(solver)
                .address(userClaimRequest.getAddress())
                .build();
        return azraelClient.getSignature(command);
    }

    public String getUserPlatformUsername(Principal user, Platform platform) {
        if (platform != Platform.GITHUB) {
            throw new RuntimeException("only github is supported for now");
        }
        return keycloakRepository.getUserIdentities(user.getName())
                .filter(i -> i.getProvider().equalsIgnoreCase(platform.toString()))
                .findFirst().orElseThrow(() -> new RuntimeException("Please link your github account!"))
                .getUsername();
    }


}
