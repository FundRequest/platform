package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.platform.github.CreateGithubComment;
import io.fundrequest.platform.github.GithubCommentFactory;
import io.fundrequest.platform.github.GithubGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CreateGithubCommentOnFundHandler {

    private final GithubGateway githubGateway;
    private final GithubCommentFactory githubCommentFactory;
    private final RequestService requestService;
    private final Boolean addComment;
    private final String githubUser;

    public CreateGithubCommentOnFundHandler(final GithubGateway githubGateway,
                                            final GithubCommentFactory githubCommentFactory,
                                            final RequestService requestService,
                                            @Value("${github.add-comments:false}") final Boolean addComment,
                                            @Value("${feign.client.github.username:fundrequest-notifier}") final String githubUser) {
        this.githubGateway = githubGateway;
        this.githubCommentFactory = githubCommentFactory;
        this.requestService = requestService;
        this.addComment = addComment;
        this.githubUser = githubUser;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createGithubCommentOnRequestFunded(final RequestFundedEvent event) {
        if (addComment) {
            final IssueInformationDto issueInformation = requestService.findRequest(event.getRequestId()).getIssueInformation();
            if (issueInformation.getPlatform() == Platform.GITHUB
                && !isCommentAlreadyPresent(issueInformation.getNumber(), issueInformation.getOwner(), issueInformation.getRepo())) {
                placeComment(event.getRequestId(), issueInformation.getNumber(), issueInformation.getOwner(), issueInformation.getRepo());
            }
        }
    }

    private void placeComment(final Long requestId, final String issueNumber, final String issueOwner, final String issueRepo) {
        final CreateGithubComment comment = new CreateGithubComment();
        comment.setBody(githubCommentFactory.createFundedComment(requestId, issueNumber));
        githubGateway.createCommentOnIssue(issueOwner, issueRepo, issueNumber, comment);
    }

    private boolean isCommentAlreadyPresent(final String issueNumber, final String issueOwner, final String issueRepo) {
        githubGateway.evictCommentsForIssue(issueOwner, issueRepo, issueNumber);
        return githubGateway.getCommentsForIssue(issueOwner, issueRepo, issueNumber)
                            .stream()
                            .anyMatch(c -> githubUser.equalsIgnoreCase(c.getUser().getLogin()));
    }
}
