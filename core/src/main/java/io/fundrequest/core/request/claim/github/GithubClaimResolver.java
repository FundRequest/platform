package io.fundrequest.core.request.claim.github;

import io.fundrequest.core.request.claim.SignedClaim;
import io.fundrequest.core.request.claim.UserClaimRequest;
import io.fundrequest.core.request.claim.dto.ClaimableResultDto;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.request.infrastructure.azrael.ClaimSignature;
import io.fundrequest.core.request.infrastructure.azrael.SignClaimCommand;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.platform.github.scraper.GithubScraper;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.keycloak.UserIdentity;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;
import java.util.function.Supplier;

import static io.fundrequest.core.request.domain.RequestStatus.CLAIMABLE;
import static io.fundrequest.core.request.domain.RequestStatus.FUNDED;

@Component
public class GithubClaimResolver {

    private static final Supplier<RuntimeException> GITHUB_ACCOUNT_IS_NOT_LINKED = () -> new RuntimeException("Github account is not linked");
    private static final String GITHUB_STATE_CLOSED = "closed";

    private GithubScraper githubScraper;
    private AzraelClient azraelClient;
    private KeycloakRepository keycloakRepository;

    public GithubClaimResolver(final GithubScraper githubScraper,
                               final AzraelClient azraelClient,
                               final KeycloakRepository keycloakRepository) {
        this.githubScraper = githubScraper;
        this.azraelClient = azraelClient;
        this.keycloakRepository = keycloakRepository;
    }

    public SignedClaim getSignedClaim(final Principal user, final UserClaimRequest userClaimRequest, final RequestDto request) {
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
    }

    public Boolean canClaim(final Principal user, final RequestDto request) {
        final String owner = request.getIssueInformation().getOwner();
        final String repo = request.getIssueInformation().getRepo();
        final String number = request.getIssueInformation().getNumber();
        final GithubIssue githubIssue = githubScraper.fetchGithubIssue(owner, repo, number);
        return isIssueClosed(githubIssue) && isClaimalbeByLoggedInUser(user, request, githubIssue.getSolver());
    }

    public ClaimableResultDto claimableResult(final String owner, final String repo, final String number, final RequestStatus requestStatus) {
        final GithubIssue githubIssue = githubScraper.fetchGithubIssue(owner, repo, number);
        if (isIssueClosed(githubIssue) && githubIssue.getSolver() != null && (requestStatus == FUNDED || requestStatus == CLAIMABLE)) {
            return ClaimableResultDto.builder()
                                     .claimable(true)
                                     .platform(Platform.GITHUB)
                                     .claimableByPlatformUserName(githubIssue.getSolver())
                                     .build();
        }
        return ClaimableResultDto.builder().claimable(false).platform(Platform.GITHUB).build();
    }

    private Boolean isClaimalbeByLoggedInUser(final Principal user, final RequestDto request, final String solver) {
        return user == null || solver == null
               ? false
               : getUserPlatformUsername(user, request.getIssueInformation().getPlatform()).map(u -> u.equalsIgnoreCase(solver)).orElse(false);
    }

    private boolean isIssueClosed(final GithubIssue githubIssue) {
        return githubIssue.getStatus().equalsIgnoreCase(GITHUB_STATE_CLOSED);
    }

    private String getSolver(final Principal user, final UserClaimRequest userClaimRequest, final RequestDto request) {
        final IssueInformationDto issueInformation = request.getIssueInformation();
        final String solver = Optional.ofNullable(githubScraper.fetchGithubIssue(issueInformation.getOwner(),
                                                                                 issueInformation.getRepo(),
                                                                                 issueInformation.getNumber()).getSolver())
                                      .orElseThrow(() -> new RuntimeException("Unable to get solver"));
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
