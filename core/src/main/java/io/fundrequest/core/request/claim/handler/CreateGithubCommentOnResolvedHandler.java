package io.fundrequest.core.request.claim.handler;

import io.fundrequest.core.request.claim.event.RequestClaimableEvent;
import io.fundrequest.core.request.claim.github.GithubSolverResolver;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.platform.github.CreateGithubComment;
import io.fundrequest.platform.github.GitHubCommentFactory;
import io.fundrequest.platform.github.GithubGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CreateGithubCommentOnResolvedHandler {

    private final GithubGateway githubGateway;
    private final GithubSolverResolver githubSolverResolver;
    private final GitHubCommentFactory gitHubCommentFactory;
    private final Boolean addComment;

    public CreateGithubCommentOnResolvedHandler(final GithubGateway githubGateway,
                                                final GithubSolverResolver githubSolverResolver,
                                                final GitHubCommentFactory gitHubCommentFactory,
                                                @Value("${github.add-comments:false}") final Boolean addComment) {
        this.githubGateway = githubGateway;
        this.githubSolverResolver = githubSolverResolver;
        this.gitHubCommentFactory = gitHubCommentFactory;
        this.addComment = addComment;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createGithubCommentOnRequestClaimable(final RequestClaimableEvent event) {
        if (addComment) {
            final RequestDto request = event.getRequestDto();
            final IssueInformationDto issueInformation = request.getIssueInformation();
            if (issueInformation.getPlatform() == Platform.GITHUB) {
                final String solver = githubSolverResolver.solveResolver(request).orElseThrow(() -> new RuntimeException("No solver found for request " + request.getId()));
                final CreateGithubComment comment = new CreateGithubComment();
                comment.setBody(gitHubCommentFactory.createResolvedComment(request.getId(), solver));
                githubGateway.createCommentOnIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber(), comment);
            }
        }
    }
}
