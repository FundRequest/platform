package io.fundrequest.core.request.fund;

import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.fund.command.PendingFundCommand;
import io.fundrequest.core.request.fund.domain.PendingFund;
import io.fundrequest.core.request.fund.dto.PendingFundDto;
import io.fundrequest.core.request.fund.infrastructure.PendingFundRepository;
import io.fundrequest.core.request.infrastructure.github.parser.GithubPlatformIdParser;
import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.Principal;
import java.util.List;

@Service
public class PendingFundService {

    private final PendingFundRepository pendingFundRepository;

    private final GithubPlatformIdParser githubLinkParser;

    private final Mappers mappers;

    private TokenInfoService tokenInfoService;

    public PendingFundService(final PendingFundRepository pendingFundRepository,
                              GithubPlatformIdParser githubLinkParser,
                              Mappers mappers, TokenInfoService tokenInfoService) {
        this.pendingFundRepository = pendingFundRepository;
        this.githubLinkParser = githubLinkParser;
        this.mappers = mappers;
        this.tokenInfoService = tokenInfoService;
    }

    @Transactional
    public void save(Principal principal, final PendingFundCommand command) {
        IssueInformation issueInformation = githubLinkParser.parseIssue(command.getPlatformId());
        PendingFund pf = PendingFund.builder()
                                    .amount(toWei(command))
                                    .description(command.getDescription())
                                    .fromAddress(command.getFromAddress())
                                    .tokenAddress(command.getTokenAddress())
                                    .transactionhash(command.getTransactionId())
                                    .issueInformation(issueInformation)
                                    .userId(principal.getName())
                                    .build();
        pendingFundRepository.save(pf);
    }

    @NotNull
    private BigInteger toWei(PendingFundCommand command) {
        TokenInfoDto tokenInfo = tokenInfoService.getTokenInfo(command.getTokenAddress());
        final BigDecimal multiplier = BigDecimal.TEN.pow(tokenInfo.getDecimals());
        return new BigDecimal(command.getAmount()).multiply(multiplier).toBigInteger();
    }

    @Transactional(readOnly = true)
    public List<PendingFundDto> findByUser(Principal principal) {
        return mappers.mapList(
                PendingFund.class,
                PendingFundDto.class,
                pendingFundRepository.findByUserId(principal.getName())
                              );

    }
}
