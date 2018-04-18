package io.fundrequest.core.config;

import io.fundrequest.core.contract.domain.FundRequestContract;
import io.fundrequest.core.contract.domain.TokenWhitelistPreconditionContract;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;

@Configuration
public class ContractConfig {


    @Bean
    public FundRequestContract fundRequestContract(final @Value("${io.fundrequest.contract.fund-request.address}") String fundRequestContractAddress,
                                                   final Web3j web3j) {
        return new FundRequestContract(fundRequestContractAddress, web3j);
    }

    @Bean
    public TokenWhitelistPreconditionContract tokenWhitelistPrecondition(final @Value("${io.fundrequest.contract.token-whitelist-precondition.address}")
                                                                                 String tokenWhitelistPreconditionContract,
                                                                         final Web3j web3j) {
        return new TokenWhitelistPreconditionContract(tokenWhitelistPreconditionContract, web3j);
    }
}