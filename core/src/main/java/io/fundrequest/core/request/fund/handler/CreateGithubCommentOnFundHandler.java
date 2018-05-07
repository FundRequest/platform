package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
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
    private final Boolean addComment;
    private final String githubUser;

    public CreateGithubCommentOnFundHandler(final GithubGateway githubGateway,
                                            final GitHubCommentFactory gitHubCommentFactory,
                                            @Value("${github.add-comments:false}") final Boolean addComment,
                                            @Value("${feign.client.github.username:fundrequest-notifier}") final String githubUser) {
        this.githubGateway = githubGateway;
        this.gitHubCommentFactory = gitHubCommentFactory;
        this.addComment = addComment;
        this.githubUser = githubUser;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createGithubCommentOnRequestFunded(final RequestFundedEvent event) {
        RequestDto request = event.getRequestDto();
        IssueInformationDto issueInformation = request.getIssueInformation();
        if (addComment && issueInformation.getPlatform() == Platform.GITHUB) {
            List<GithubIssueCommentsResult> comments = githubGateway.getCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber());
            Optional<GithubIssueCommentsResult> existingComments = comments.stream().filter(c -> githubUser.equalsIgnoreCase(c.getUser().getLogin())).findFirst();
            if (!existingComments.isPresent()) {
                CreateGithubComment comment = new CreateGithubComment();
                comment.setBody(gitHubCommentFactory.createFundedComment(request.getId()));
                githubGateway.createCommentOnIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber(), comment);
            }
        }
    }
}
