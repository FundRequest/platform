package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.platform.github.CreateGithubComment;
import io.fundrequest.platform.github.GitHubCommentFactory;
import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.parser.GithubIssueCommentsResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.Optional;

@Component
public class CreateGithubCommentOnFundHandler {

    private final GithubGateway githubGateway;
    private final GitHubCommentFactory gitHubCommentFactory;
    private final RequestService requestService;
    private final Boolean addComment;
    private final String githubUser;

    public CreateGithubCommentOnFundHandler(final GithubGateway githubGateway,
                                            final GitHubCommentFactory gitHubCommentFactory,
                                            final RequestService requestService,
                                            @Value("${github.add-comments:false}") final Boolean addComment,
                                            @Value("${feign.client.github.username:fundrequest-notifier}") final String githubUser) {
        this.githubGateway = githubGateway;
        this.gitHubCommentFactory = gitHubCommentFactory;
        this.requestService = requestService;
        this.addComment = addComment;
        this.githubUser = githubUser;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createGithubCommentOnRequestFunded(final RequestFundedEvent event) {
        if (addComment) {
            final IssueInformationDto issueInformation = requestService.findRequest(event.getRequestId()).getIssueInformation();
            if (issueInformation.getPlatform() == Platform.GITHUB) {
                githubGateway.evictCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber());
                final List<GithubIssueCommentsResult> comments = githubGateway.getCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber());
                final Optional<GithubIssueCommentsResult> existingComments = comments.stream().filter(c -> githubUser.equalsIgnoreCase(c.getUser().getLogin())).findFirst();
                if (!existingComments.isPresent()) {
                    final CreateGithubComment comment = new CreateGithubComment();
                    comment.setBody(gitHubCommentFactory.createFundedComment(event.getRequestId()));
                    githubGateway.createCommentOnIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber(), comment);
                }
            }
        }
    }
}
