package io.fundrequest.core.request.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Getter
@Table(name = "blockchain_event")
public class BlockchainEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_hash")
    private String transactionHash;

    @Column(name = "log_index")
    private String logIndex;

    @Column(name = "process_date")
    private LocalDateTime processDate;

    protected BlockchainEvent() {
    }

    public BlockchainEvent(final String transactionHash, final String logIndex) {
        this.transactionHash = transactionHash;
        this.logIndex = logIndex;
        this.processDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BlockchainEvent that = (BlockchainEvent) o;
        return Objects.equals(transactionHash, that.transactionHash)
               && Objects.equals(logIndex, that.logIndex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionHash, logIndex);
    }
}
