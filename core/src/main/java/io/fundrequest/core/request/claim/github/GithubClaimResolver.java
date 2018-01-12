package io.fundrequest.core.request.claim.github;

import io.fundrequest.core.keycloak.KeycloakRepository;
import io.fundrequest.core.request.claim.SignClaimRequest;
import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.request.infrastructure.azrael.ClaimSignature;
import io.fundrequest.core.request.infrastructure.azrael.SignClaimCommand;
import io.fundrequest.core.request.view.RequestDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;

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

    public SignedClaim getSignedClaim(Principal user, SignClaimRequest signClaimRequest, RequestDto request) {
        try {
            String solver = getSolver(user, signClaimRequest, request);
            ClaimSignature signature = getSignature(signClaimRequest, solver);
            return new SignedClaim(
                    solver,
                    signature.getAddress(),
                    signClaimRequest.getPlatform(),
                    signature.getPlatformId(),
                    signature.getR(),
                    signature.getS(),
                    signature.getV());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private String getSolver(Principal user, SignClaimRequest signClaimRequest, RequestDto request) throws IOException {
        String solver = githubSolverResolver.solveResolver(request);
        if (!solver.equals(getUserPlatformUsername(user, signClaimRequest.getPlatform()))) {
            throw new RuntimeException("Claim executed by wrong user");
        }
        return solver;
    }

    private ClaimSignature getSignature(SignClaimRequest signClaimRequest, String solver) {
        SignClaimCommand command = new SignClaimCommand();
        command.setPlatform(signClaimRequest.getPlatform().toString());
        command.setPlatformId(signClaimRequest.getPlatformId());
        command.setSolver(solver);
        command.setAddress(signClaimRequest.getAddress());
        return azraelClient.getSignature(command);
    }

    private String getUserPlatformUsername(Principal user, Platform platform) {
        if (platform != Platform.GITHUB) {
            throw new RuntimeException("only github is supported for now");
        }
        return keycloakRepository.getUserIdentities(user.getName())
                .filter(i -> i.getProvider().equalsIgnoreCase(platform.toString()))
                .findFirst().orElseThrow(() -> new RuntimeException("Please link your github account!"))
                .getUsername();
    }


}
