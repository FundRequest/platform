package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.infrastructure.github.CreateGithubComment;
import io.fundrequest.core.request.infrastructure.github.GithubClient;
import io.fundrequest.core.user.UserDto;
import io.fundrequest.core.user.UserService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CreateGithubCommentOnFundHandler {

    private GithubClient githubClient;
    private UserService userService;

    public CreateGithubCommentOnFundHandler(GithubClient githubClient, UserService userService) {
        this.githubClient = githubClient;
        this.userService = userService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createGithubCommentOnRequestFunded(RequestFundedEvent event) {
        UserDto user = userService.getUser(event.getFunder());
        CreateGithubComment comment = new CreateGithubComment();
        double amount = event.getAmountInWei() / Math.pow(10, 18);
        comment.setBody("Great! " + user.getName() + " funded " + amount + " FND to this issue. For more information, go to https://fundrequest.io.");
        githubClient.createCommandOnIssue(event.getOwner(), event.getRepo(), event.getNumber(), comment);
    }
}
