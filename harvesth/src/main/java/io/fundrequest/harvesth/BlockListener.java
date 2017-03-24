package io.fundrequest.harvesth;

import org.ethereum.core.BlockSummary;
import org.ethereum.facade.Ethereum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class BlockListener {

    private Ethereum ethereum;

    @Autowired
    public BlockListener(Ethereum ethereum) {
        this.ethereum = ethereum;
    }

    @PostConstruct
    public void registerListener() {
        this.ethereum.addListener(new InternalBlockListener());
    }

    private class InternalBlockListener implements HarvesthEthereumListener {
        @Override
        public void onBlock(BlockSummary blockSummary) {
            logger.info(
                    "Received block {} with {} transactions",
                    blockSummary.getBlock().getNumber(),
                    blockSummary.getBlock().getTransactionsList().size()
            );
        }
    }

}
