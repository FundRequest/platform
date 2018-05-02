package io.fundrequest.core.request.claim.github;

import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.claim.UserClaimRequest;
import io.fundrequest.core.request.claim.dto.UserClaimableDto;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.request.infrastructure.azrael.ClaimSignature;
import io.fundrequest.core.request.infrastructure.azrael.SignClaimCommand;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.keycloak.UserIdentity;
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
               && solver.get().equalsIgnoreCase(getUserPlatformUsername(user, request.getIssueInformation().getPlatform()).orElseThrow(() -> new RuntimeException(
                "Github account is not linked")));
    }

    public UserClaimableDto userClaimableResult(Principal user, RequestDto request) {
        Optional<String> solver = githubSolverResolver.solveResolver(request);
        if (solver.isPresent() && request.getStatus() == RequestStatus.FUNDED || request.getStatus() == RequestStatus.CLAIMABLE) {
            return UserClaimableDto.builder()
                                   .claimable(true)
                                   .claimableByUser(getUserPlatformUsername(user, request.getIssueInformation().getPlatform()).map(u -> u.equalsIgnoreCase(solver.get()))
                                                                                                                              .orElse(false))
                                   .build();
        }
        return UserClaimableDto.builder().claimable(false).claimableByUser(false).build();
    }

    private String getSolver(Principal user, UserClaimRequest userClaimRequest, RequestDto request) throws IOException {
        String solver = githubSolverResolver.solveResolver(request).orElseThrow(() -> new RuntimeException("Unable to get solver"));
        if (!solver.equalsIgnoreCase(getUserPlatformUsername(user, userClaimRequest.getPlatform()).orElseThrow(() -> new RuntimeException("Github account is not linked")))) {
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

    public Optional<String> getUserPlatformUsername(Principal user, Platform platform) {
        if (platform != Platform.GITHUB) {
            throw new RuntimeException("only github is supported for now");
        }
        return keycloakRepository.getUserIdentities(user.getName())
                                 .filter(i -> i.getProvider().name().equalsIgnoreCase(platform.toString()))
                                 .findFirst().map(UserIdentity::getUsername);
    }


}
