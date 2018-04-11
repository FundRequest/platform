package io.fundrequest.core.erc20.service;

import io.fundrequest.core.erc20.domain.HumanStandardToken;
import io.fundrequest.core.web3j.Web3jGateway;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;


@Component
public class ERC20Service {

    private Web3jGateway web3jGateway;

    public ERC20Service(final Web3jGateway web3jGateway) {
        this.web3jGateway = web3jGateway;
    }

    @Cacheable(cacheNames = "erc20.tokens.decimals", key = "#token")
    public int decimals(final String token) {
        return getERC20(token).decimals();
    }

    @Cacheable(cacheNames = "erc20.tokens.name", key = "#token")
    public String name(final String token) {
        return getERC20(token).name();
    }

    @Cacheable(cacheNames = "erc20.tokens.symbol", key = "#token")
    public String symbol(final String token) {
        return getERC20(token).symbol();
    }

    private HumanStandardToken getERC20(final String token) {
        return HumanStandardToken.load(token, web3jGateway.web3j());
    }
}
