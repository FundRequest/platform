package io.fundrequest.core.request.fund.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "processed_blockchain_event")
public class ProcessedBlockchainEvent {
    @Id
    @Column(name = "transaction_hash")
    private String transactionHash;

    @Column(name = "process_date")
    private LocalDateTime processDate;

    protected ProcessedBlockchainEvent() {
    }

    public ProcessedBlockchainEvent(String transactionHash) {
        this.transactionHash = transactionHash;
        this.processDate = LocalDateTime.now();
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public LocalDateTime getProcessDate() {
        return processDate;
    }
}
