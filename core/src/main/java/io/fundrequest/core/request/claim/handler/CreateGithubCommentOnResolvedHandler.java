package io.fundrequest.core.request.claim.handler;

import io.fundrequest.core.request.claim.event.RequestClaimableEvent;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.platform.github.CreateGithubComment;
import io.fundrequest.platform.github.GithubCommentFactory;
import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.scraper.GithubScraper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Optional;

@Component
public class CreateGithubCommentOnResolvedHandler {

    private final GithubGateway githubGateway;
    private final GithubScraper githubScraper;
    private final GithubCommentFactory githubCommentFactory;
    private final Boolean addComment;

    public CreateGithubCommentOnResolvedHandler(final GithubGateway githubGateway,
                                                final GithubScraper githubScraper,
                                                final GithubCommentFactory githubCommentFactory,
                                                @Value("${github.add-comments:false}") final Boolean addComment) {
        this.githubGateway = githubGateway;
        this.githubScraper = githubScraper;
        this.githubCommentFactory = githubCommentFactory;
        this.addComment = addComment;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createGithubCommentOnRequestClaimable(final RequestClaimableEvent event) {
        if (addComment) {
            final RequestDto request = event.getRequestDto();
            final IssueInformationDto issueInformation = request.getIssueInformation();
            if (issueInformation.getPlatform() == Platform.GITHUB) {
                final String solver = Optional.ofNullable(githubScraper.fetchGithubIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber()).getSolver())
                                              .orElseThrow(() -> new RuntimeException("No solver found for request " + request.getId()));
                final CreateGithubComment comment = new CreateGithubComment();
                comment.setBody(githubCommentFactory.createResolvedComment(request.getId(), solver));
                githubGateway.createCommentOnIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber(), comment);
            }
        }
    }
}
