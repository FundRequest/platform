package io.fundrequest.core.request.claim.handler;

import io.fundrequest.core.request.claim.event.RequestClaimedEvent;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.platform.github.CreateGithubComment;
import io.fundrequest.platform.github.GithubCommentFactory;
import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.parser.GithubIssueCommentsResult;
import io.fundrequest.platform.github.scraper.GithubScraper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CreateGithubCommentOnClosedHandler {

    private final GithubGateway githubGateway;
    private final GithubScraper githubScraper;
    private final GithubCommentFactory githubCommentFactory;
    private final Boolean addComment;
    private final String githubUser;

    public CreateGithubCommentOnClosedHandler(final GithubGateway githubGateway,
                                              final GithubScraper githubScraper,
                                              final GithubCommentFactory githubCommentFactory,
                                              @Value("${github.add-comments:false}") final Boolean addComment,
                                              @Value("${feign.client.github.username:fundrequest-notifier}") final String githubUser) {

        this.githubGateway = githubGateway;
        this.githubScraper = githubScraper;
        this.githubCommentFactory = githubCommentFactory;
        this.addComment = addComment;
        this.githubUser = githubUser;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createGithubCommentOnRequestClaimed(final RequestClaimedEvent event) {
        if (addComment) {
            final RequestDto request = event.getRequestDto();
            final IssueInformationDto issueInformation = request.getIssueInformation();
            if (issueInformation.getPlatform() == Platform.GITHUB) {
                placeComment(request, issueInformation);
            }
        }
    }

    private void placeComment(final RequestDto request, final IssueInformationDto issueInformation) {
        final CreateGithubComment comment = createComment(request.getId(), issueInformation);
        final List<GithubIssueCommentsResult> ourComments = getOurComments(issueInformation);
        if (ourComments.size() < 2) {
            placeNewComment(issueInformation, comment);
        } else {
            editLastComment(issueInformation, comment, ourComments);
        }
    }

    private CreateGithubComment createComment(final Long requestId, final IssueInformationDto issueInformation) {
        final String solver = Optional.ofNullable(githubScraper.fetchGithubIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber()).getSolver())
                                      .orElseThrow(() -> new RuntimeException("No solver found for request " + requestId));

        final CreateGithubComment comment = new CreateGithubComment();
        comment.setBody(githubCommentFactory.createClosedComment(requestId, solver));
        return comment;
    }

    private List<GithubIssueCommentsResult> getOurComments(final IssueInformationDto issueInformation) {
        githubGateway.evictCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber());
        final List<GithubIssueCommentsResult> comments = githubGateway.getCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber());
        return comments.stream().filter(c -> githubUser.equalsIgnoreCase(c.getUser().getLogin())).collect(Collectors.toList());
    }

    private void placeNewComment(final IssueInformationDto issueInformation, final CreateGithubComment comment) {
        githubGateway.createCommentOnIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber(), comment);
    }

    private void editLastComment(final IssueInformationDto issueInformation, final CreateGithubComment comment, List<GithubIssueCommentsResult> ourComments) {
        final GithubIssueCommentsResult lastComment = ourComments.stream()
                                                                 .max(Comparator.comparing(GithubIssueCommentsResult::getCreatedAt))
                                                                 .orElseThrow(() -> new RuntimeException("No comments found"));
        githubGateway.editCommentOnIssue(issueInformation.getOwner(), issueInformation.getRepo(), lastComment.getId(), comment);
    }
}
