package io.fundrequest.core.contract.service;

import io.fundrequest.core.contract.domain.ClaimRepositoryContract;
import io.fundrequest.core.contract.domain.FundRepositoryContract;
import io.fundrequest.core.contract.domain.FundRequestContract;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class FundRequestContractsService {


    private Web3j web3j;

    private FundRequestContract fundRequestContract;
    private FundRepositoryContract fundRepositoryContract;
    private ClaimRepositoryContract claimRepositoryContract;

    public FundRequestContractsService(final FundRequestContract fundRequestContract,
                                       final Web3j web3j) {
        this.fundRequestContract = fundRequestContract;
        this.web3j = web3j;
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
            log.error("Unable to start application, couldn't access contracts");
            throw new IllegalArgumentException("Couldn't startup");
        }
    }
}
