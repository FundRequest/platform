package io.fundrequest.core.web3j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

import javax.annotation.PostConstruct;

@Component
public class Web3jGateway {

    @Autowired
    private Web3j primary;
    @Autowired
    @Qualifier("local")
    private Web3j secondary;

    private Web3j currentProvider;

    @PostConstruct
    @Scheduled(fixedDelay = 20000)
    private void init() {
        currentProvider = updateCurrentProvider();
    }

    public Web3j web3j() {
        return currentProvider;
    }

    public Web3j secondary() {
        return secondary;
    }

    private Web3j updateCurrentProvider() {
        try {
            primary.ethBlockNumber().send();
            return primary;
        } catch (final Exception ex) {
            return secondary;
        }
    }
}
