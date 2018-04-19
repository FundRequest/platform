package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.fund.command.PendingFundCommand;
import io.fundrequest.core.request.fund.domain.PendingFund;
import io.fundrequest.core.request.fund.infrastructure.PendingFundRepository;
import io.fundrequest.core.request.infrastructure.github.parser.GithubPlatformIdParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.security.Principal;

@Service
public class PendingFundService {

    private final PendingFundRepository pendingFundRepository;

    private final GithubPlatformIdParser githubLinkParser;

    public PendingFundService(final PendingFundRepository pendingFundRepository, GithubPlatformIdParser githubLinkParser) {
        this.pendingFundRepository = pendingFundRepository;
        this.githubLinkParser = githubLinkParser;
    }

    @Transactional
    public void save(Principal principal, final PendingFundCommand command) {
        IssueInformation issueInformation = githubLinkParser.parseIssue(command.getPlatformId());
        PendingFund pf = PendingFund.builder()
                                    .amount(new BigInteger(command.getAmount()))
                                    .description(command.getDescription())
                                    .fromAddress(command.getFromAddress())
                                    .tokenAddress(command.getTokenAddress())
                                    .transactionhash(command.getTransactionId())
                                    .issueInformation(issueInformation)
                                    .userId(principal.getName())
                                    .build();
        pendingFundRepository.save(pf);
    }
}
