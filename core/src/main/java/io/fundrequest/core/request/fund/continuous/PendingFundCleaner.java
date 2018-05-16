package io.fundrequest.core.request.fund.continuous;

import io.fundrequest.core.request.fund.PendingFundService;
import io.fundrequest.core.request.fund.domain.PendingFund;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.transactions.TransactionStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static io.fundrequest.core.web3j.AddressUtils.prettify;

@Component
@Slf4j
@ConditionalOnProperty(value = "io.fundrequest.pending-fund-cleaner.enabled", havingValue = "true")
public class PendingFundCleaner {

    private final PendingFundService pendingFundService;
    private final AzraelClient azraelClient;

    public PendingFundCleaner(final PendingFundService pendingFundService,
                              final AzraelClient azraelClient) {
        this.pendingFundService = pendingFundService;
        this.azraelClient = azraelClient;
    }

    @Scheduled(fixedDelay = 300_000 /* 5 minutes */)
    public void cleanupPendingFunds() {
        pendingFundService.findAll()
                          .stream()
                          .filter(fund -> {
                              try {
                                  return hasFailed(fund) || hasAged(fund);
                              } catch (final Exception ex) {
                                  log.error("Unable to fetch status for transaction {} : ", fund.getTransactionHash(), ex.getMessage());
                                  return false;
                              }
                          })
                          .forEach(pendingFundService::removePendingFund);
    }

    private boolean hasAged(PendingFund fund) {
        return fund.getCreationDate().isBefore(LocalDateTime.now().minusDays(1));
    }

    private boolean hasFailed(PendingFund fund) {
        return azraelClient.getTransactionStatus(prettify(fund.getTransactionHash())).equals(TransactionStatus.FAILED);
    }
}
