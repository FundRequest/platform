package io.fundrequest.core.transactions;

import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import org.springframework.stereotype.Component;

@Component
public class TransactionService {

    private final AzraelClient azraelClient;

    public TransactionService(AzraelClient azraelClient) {
        this.azraelClient = azraelClient;
    }

    public TransactionStatus getTransactionStatus(final String transactionHash) {
        return azraelClient.getTransactionStatus(transactionHash);
    }
}
