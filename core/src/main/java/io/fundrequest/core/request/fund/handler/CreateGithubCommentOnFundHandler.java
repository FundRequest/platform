package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.infrastructure.github.CreateGithubComment;
import io.fundrequest.core.request.infrastructure.github.GithubClient;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

@Component
public class CreateGithubCommentOnFundHandler {

    private GithubClient githubClient;
    private Boolean addComment;

    public CreateGithubCommentOnFundHandler(GithubClient githubClient, @Value("${github.add-comment-when-funded:false}") Boolean addComment) {
        this.githubClient = githubClient;
        this.addComment = addComment;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void createGithubCommentOnRequestFunded(RequestFundedEvent event) {
        if (addComment) {
            CreateGithubComment comment = new CreateGithubComment();
            FundDto fundDto = event.getFundDto();
            BigDecimal amount = Convert.fromWei(fundDto.getAmountInWei(), Convert.Unit.ETHER);
            comment.setBody("Great! " + amount.toString() + " FND was added to this issue. For more information, go to https://alpha.fundrequest.io.");
            RequestDto request = event.getRequestDto();
            IssueInformationDto issueInformation = request.getIssueInformation();
            if (issueInformation.getPlatform() == Platform.GITHUB) {
                githubClient.createCommentOnIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber(), comment);
            }
        }
    }
}
