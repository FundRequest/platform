package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.infrastructure.github.CreateGithubComment;
import io.fundrequest.core.request.infrastructure.github.GithubClient;
import io.fundrequest.core.user.UserDto;
import io.fundrequest.core.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

@Component
public class CreateGithubCommentOnFundHandler {

    private GithubClient githubClient;
    private UserService userService;
    private Boolean addComment;

    public CreateGithubCommentOnFundHandler(GithubClient githubClient, UserService userService, @Value("${github.add-comment-when-funded}") Boolean addComment) {
        this.githubClient = githubClient;
        this.userService = userService;
        this.addComment = addComment;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createGithubCommentOnRequestFunded(RequestFundedEvent event) {
        if (addComment) {
            UserDto user = userService.getUser(event.getFunder());
            CreateGithubComment comment = new CreateGithubComment();
            BigDecimal amount = Convert.fromWei(event.getAmountInWei(), Convert.Unit.ETHER);
            String funder = (user == null || StringUtils.isEmpty(user.getEmail())) ? "Anonymous" : user.getEmail();
            comment.setBody("Great! " + funder + " funded " + amount.toString() + " FND to this issue. For more information, go to https://fundrequest.io.");
            githubClient.createCommentOnIssue(event.getOwner(), event.getRepo(), event.getNumber(), comment);
        }
    }
}
