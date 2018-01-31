package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.infrastructure.github.CreateGithubComment;
import io.fundrequest.core.request.infrastructure.github.GithubClient;
import io.fundrequest.core.request.infrastructure.github.parser.GithubIssueCommentsResult;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.web3j.utils.Convert;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Component
public class CreateGithubCommentOnFundHandler {

    private GithubClient githubClient;
    private Boolean addComment;
    private String githubUser;

    public CreateGithubCommentOnFundHandler(
            GithubClient githubClient,
            @Value("${github.add-comment-when-funded:false}") Boolean addComment,
            @Value("${feign.client.github.username:fundrequest-notifier}") String githubUser) {
        this.githubClient = githubClient;
        this.addComment = addComment;
        this.githubUser = githubUser;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createGithubCommentOnRequestFunded(RequestFundedEvent event) {
        RequestDto request = event.getRequestDto();
        IssueInformationDto issueInformation = request.getIssueInformation();
        if (addComment && issueInformation.getPlatform() == Platform.GITHUB) {
            List<GithubIssueCommentsResult> comments = githubClient.getCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber());
            Optional<GithubIssueCommentsResult> existingComments = comments.stream().filter(c -> githubUser.equalsIgnoreCase(c.getUser().getLogin())).findFirst();
            CreateGithubComment comment = new CreateGithubComment();
            comment.setBody(getNewFundingComment(event));
            if (existingComments.isPresent()) {
                comment.setBody(existingComments.get().getBody() + comment.getBody());
                githubClient.editCommentOnIssue(issueInformation.getOwner(), issueInformation.getRepo(), existingComments.get().getId(), comment);
            } else {
                comment.setBody("Great, this issue is now funded on FundRequest: https://alpha.fundrequest.io!  \n" + comment.getBody());
                githubClient.createCommentOnIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber(), comment);
            }
        }
    }

    private String getNewFundingComment(RequestFundedEvent event) {
        String fundingComment = "\n" + Convert.fromWei(event.getFundDto().getAmountInWei(), Convert.Unit.ETHER).toString() + " FND was funded on ";
        fundingComment += DateTimeFormatter.ISO_DATE.format(event.getFundDto().getTimestamp());
        return fundingComment;
    }
}
