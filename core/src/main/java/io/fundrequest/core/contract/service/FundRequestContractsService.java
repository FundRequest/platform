package io.fundrequest.core.contract.service;

import io.fundrequest.core.contract.domain.ClaimRepositoryContract;
import io.fundrequest.core.contract.domain.FundRepositoryContract;
import io.fundrequest.core.contract.domain.FundRequestContract;
import io.fundrequest.core.contract.domain.TokenWhitelistPreconditionContract;
import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@Slf4j
public class FundRequestContractsService {


    private TokenWhitelistPreconditionContract tokenWhitelistPreconditionContract;
    private Web3j web3j;

    private FundRequestContract fundRequestContract;
    private FundRepositoryContract fundRepositoryContract;
    private ClaimRepositoryContract claimRepositoryContract;
    private TokenInfoService tokenInfoService;

    public FundRequestContractsService(final FundRequestContract fundRequestContract,
                                       final TokenWhitelistPreconditionContract tokenWhitelistPreconditionContract,
                                       final Web3j web3j,
                                       final TokenInfoService tokenInfoService) {
        this.fundRequestContract = fundRequestContract;
        this.tokenWhitelistPreconditionContract = tokenWhitelistPreconditionContract;
        this.web3j = web3j;
        this.tokenInfoService = tokenInfoService;
    }

    public FundRepositoryContract fundRepository() {
        return fundRepositoryContract;
    }

    public ClaimRepositoryContract claimRepository() {
        return claimRepositoryContract;
    }

    @PostConstruct
    public void init() {
        try {
            this.fundRepositoryContract = new FundRepositoryContract(
                    fundRequestContract.fundRepository().send(),
                    web3j
            );
            this.claimRepositoryContract = new ClaimRepositoryContract(
                    fundRequestContract.claimRepository().send(),
                    web3j
            );
        } catch (final Exception ex) {
            log.error("Unable to start application, couldn't access contracts", ex);
            throw new IllegalArgumentException("Couldn't startup");
        }
    }

    @Cacheable(value = "possible_tokens", key = "#platform + '-' + #platformId")
    public List<TokenInfoDto> getAllPossibleTokens(final String platform, final String platformId) {
        return getAllPossibleTokens()
                .stream()
                .filter(token -> tokenWhitelistPreconditionContract.isValid(platform, platformId, token.getAddress()))
                .collect(Collectors.toList());
    }

    private List<TokenInfoDto> getAllPossibleTokens() {
        try {
            final int amount = tokenWhitelistPreconditionContract.amountOftokens().send().intValue();
            return IntStream.range(0, amount)
                            .mapToObj(x -> tokenWhitelistPreconditionContract.token(BigInteger.valueOf(x))).filter(Optional::isPresent)
                            .map(Optional::get)
                            .map(x -> tokenInfoService.getTokenInfo(x))
                            .collect(Collectors.toList());
        } catch (final Exception exception) {
            log.debug("Unable to fetch all possible tokens from contract", exception);
            return Collections.emptyList();
        }
    }
}
