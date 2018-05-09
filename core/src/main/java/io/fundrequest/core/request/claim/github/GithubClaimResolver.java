package io.fundrequest.core.request.claim.github;

import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.claim.UserClaimRequest;
import io.fundrequest.core.request.claim.dto.UserClaimableDto;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.request.infrastructure.azrael.ClaimSignature;
import io.fundrequest.core.request.infrastructure.azrael.SignClaimCommand;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.platform.github.GithubSolverResolver;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.keycloak.UserIdentity;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class GithubClaimResolver {

    private static final Supplier<RuntimeException> GITHUB_ACCOUNT_IS_NOT_LINKED = () -> new RuntimeException("Github account is not linked");

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
            return new SignedClaim(solver,
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

    public Boolean canClaim(final Principal user, final RequestDto request) {
        final Optional<String> solver = getSolver(request);
        final String userPlatformUsername = getUserPlatformUsername(user, request.getIssueInformation().getPlatform()).orElseThrow(GITHUB_ACCOUNT_IS_NOT_LINKED);
        return solver.isPresent() && solver.get().equalsIgnoreCase(userPlatformUsername);
    }

    public UserClaimableDto userClaimableResult(final Principal user, RequestDto request) {
        final Optional<String> solver = getSolver(request);
        if (solver.isPresent() && (request.getStatus() == RequestStatus.FUNDED || request.getStatus() == RequestStatus.CLAIMABLE)) {
            return UserClaimableDto.builder()
                                   .claimable(true)
                                   .claimableByUser(isClaimalbeByUser(user, request, solver.get()))
                                   .build();
        }
        return UserClaimableDto.builder().claimable(false).claimableByUser(false).build();
    }

    @NotNull
    private Boolean isClaimalbeByUser(Principal user, RequestDto request, String solver) {
        return user == null
               ? false
               : getUserPlatformUsername(user, request.getIssueInformation().getPlatform()).map(u -> u.equalsIgnoreCase(solver)).orElse(false);
    }

    private Optional<String> getSolver(final RequestDto request) {
        final IssueInformationDto issueInformation = request.getIssueInformation();
        return githubSolverResolver.solveResolver(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber());
    }

    private String getSolver(final Principal user, final UserClaimRequest userClaimRequest, final RequestDto request) throws IOException {
        final String solver = getSolver(request).orElseThrow(() -> new RuntimeException("Unable to get solver"));
        final String userPlatformUsername = getUserPlatformUsername(user, userClaimRequest.getPlatform()).orElseThrow(GITHUB_ACCOUNT_IS_NOT_LINKED);
        if (!solver.equalsIgnoreCase(userPlatformUsername)) {
            throw new RuntimeException("Claim executed by wrong user");
        }
        return solver;
    }

    private ClaimSignature getSignature(UserClaimRequest userClaimRequest, String solver) {
        final SignClaimCommand command = SignClaimCommand.builder()
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
                                 .findFirst()
                                 .map(UserIdentity::getUsername);
    }
}
