package io.fundrequest.core.request.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.domain.BlockchainEvent;

import java.util.Optional;

public interface BlockchainEventRepository extends JpaRepository<BlockchainEvent, Long> {
    Optional<BlockchainEvent> findByTransactionHashAndLogIndex(String transactionHash, String logIndex);
}
