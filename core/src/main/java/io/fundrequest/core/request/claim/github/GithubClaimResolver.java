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
import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.GithubSolverResolver;
import io.fundrequest.platform.github.parser.GithubResult;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.keycloak.UserIdentity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;
import java.util.Optional;
import java.util.function.Supplier;

@Component
public class GithubClaimResolver {

    private static final Supplier<RuntimeException> GITHUB_ACCOUNT_IS_NOT_LINKED = () -> new RuntimeException("Github account is not linked");
    private static final String GITHUB_STATE_CLOSED = "closed";

    private GithubSolverResolver githubSolverResolver;
    private final GithubGateway githubGateway;
    private AzraelClient azraelClient;
    private KeycloakRepository keycloakRepository;

    public GithubClaimResolver(final GithubSolverResolver githubSolverResolver,
                               final GithubGateway githubGateway,
                               final AzraelClient azraelClient,
                               final KeycloakRepository keycloakRepository) {
        this.githubSolverResolver = githubSolverResolver;
        this.githubGateway = githubGateway;
        this.azraelClient = azraelClient;
        this.keycloakRepository = keycloakRepository;
    }

    public SignedClaim getSignedClaim(final Principal user, final UserClaimRequest userClaimRequest, final RequestDto request) {
        try {
            final String solver = getSolver(user, userClaimRequest, request);
            final ClaimSignature signature = getSignature(userClaimRequest, solver);
            return SignedClaim.builder()
                              .solver(solver)
                              .solverAddress(signature.getAddress())
                              .platform(userClaimRequest.getPlatform())
                              .platformId(signature.getPlatformId())
                              .r(signature.getR())
                              .s(signature.getS())
                              .v(signature.getV())
                              .build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Boolean canClaim(final Principal user, final RequestDto request) {
        final String owner = request.getIssueInformation().getOwner();
        final String repo = request.getIssueInformation().getRepo();
        final String number = request.getIssueInformation().getNumber();
        return isIssueClosed(owner, repo, number) && getSolver(owner, repo, number).map(solver -> isClaimalbeByUser(user, request, solver)).orElse(false);
    }

    public UserClaimableDto userClaimableResult(final Principal user, final RequestDto request) {
        final IssueInformationDto issueInformation = request.getIssueInformation();
        if (isIssueClosed(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())) {
            final Optional<String> solver = getSolver(request.getIssueInformation().getOwner(), request.getIssueInformation().getRepo(), request.getIssueInformation().getNumber());
            if (solver.isPresent() && (request.getStatus() == RequestStatus.FUNDED || request.getStatus() == RequestStatus.CLAIMABLE)) {
                return UserClaimableDto.builder()
                                       .claimable(true)
                                       .claimableByUser(isClaimalbeByUser(user, request, solver.get()))
                                       .build();
            }
        }
        return UserClaimableDto.builder().claimable(false).claimableByUser(false).build();
    }

    private Boolean isClaimalbeByUser(final Principal user, final RequestDto request, final String solver) {
        return user == null
               ? false
               : getUserPlatformUsername(user, request.getIssueInformation().getPlatform()).map(u -> u.equalsIgnoreCase(solver)).orElse(false);
    }

    private boolean isIssueClosed(final String owner, final String repo, final String number) {
        githubGateway.evictIssue(owner, repo, number);
        final GithubResult githubIssue = githubGateway.getIssue(owner, repo, number);
        return githubIssue.getState().equals(GITHUB_STATE_CLOSED);
    }

    private Optional<String> getSolver(final String owner, final String repo, final String number) {
        return githubSolverResolver.resolveSolver(owner, repo, number);
    }

    private String getSolver(final Principal user, final UserClaimRequest userClaimRequest, final RequestDto request) throws IOException {
        final String solver = getSolver(request.getIssueInformation().getOwner(),
                                        request.getIssueInformation().getRepo(),
                                        request.getIssueInformation().getNumber()).orElseThrow(() -> new RuntimeException("Unable to get solver"));
        final String userPlatformUsername = getUserPlatformUsername(user, userClaimRequest.getPlatform()).orElseThrow(GITHUB_ACCOUNT_IS_NOT_LINKED);
        if (!solver.equalsIgnoreCase(userPlatformUsername)) {
            throw new RuntimeException("Claim executed by wrong user");
        }
        return solver;
    }

    private ClaimSignature getSignature(final UserClaimRequest userClaimRequest, final String solver) {
        final SignClaimCommand command = SignClaimCommand.builder()
                                                         .platform(userClaimRequest.getPlatform().toString())
                                                         .platformId(userClaimRequest.getPlatformId())
                                                         .solver(solver)
                                                         .address(userClaimRequest.getAddress())
                                                         .build();
        return azraelClient.getSignature(command);
    }

    public Optional<String> getUserPlatformUsername(final Principal user, final Platform platform) {
        if (platform != Platform.GITHUB) {
            throw new RuntimeException("only github is supported for now");
        }
        return keycloakRepository.getUserIdentities(user.getName())
                                 .filter(i -> i.getProvider().name().equalsIgnoreCase(platform.toString()))
                                 .findFirst()
                                 .map(UserIdentity::getUsername);
    }
}
