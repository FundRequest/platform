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
import io.fundrequest.core.web3j.EthUtil;
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
    private final TokenInfoService tokenInfoService;

    public PendingFundService(final PendingFundRepository pendingFundRepository,
                              final GithubPlatformIdParser githubLinkParser,
                              final Mappers mappers,
                              final TokenInfoService tokenInfoService) {
        this.pendingFundRepository = pendingFundRepository;
        this.githubLinkParser = githubLinkParser;
        this.mappers = mappers;
        this.tokenInfoService = tokenInfoService;
    }

    @Transactional(readOnly = true)
    public List<PendingFund> findAll() {
        return pendingFundRepository.findAll();
    }

    @Transactional
    public void save(Principal principal, final PendingFundCommand command) {
        final IssueInformation issueInformation = githubLinkParser.parseIssue(command.getPlatformId());
        PendingFund pf = PendingFund.builder()
                                    .amount(toWei(command))
                                    .description(command.getDescription())
                                    .fromAddress(command.getFromAddress())
                                    .tokenAddress(command.getTokenAddress())
                                    .transactionhash(command.getTransactionId())
                                    .issueInformation(issueInformation)
                                    .userId(principal == null ? null : principal.getName())
                                    .build();
        pendingFundRepository.save(pf);
    }

    @Transactional
    public void removePendingFund(final String transactionHash) {
        pendingFundRepository.findByTransactionHash(transactionHash).ifPresent(pendingFundRepository::delete);
    }

    @Transactional
    public void removePendingFund(final PendingFund pendingFund) {
        pendingFundRepository.findOne(pendingFund.getId()).ifPresent(pendingFundRepository::delete);
    }

    private BigInteger toWei(PendingFundCommand command) {
        final TokenInfoDto tokenInfo = tokenInfoService.getTokenInfo(command.getTokenAddress());
        return EthUtil.toWei(new BigDecimal(command.getAmount()), tokenInfo.getDecimals()).toBigInteger();
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
