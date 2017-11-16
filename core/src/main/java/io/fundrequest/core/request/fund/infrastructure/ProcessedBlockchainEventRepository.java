package io.fundrequest.core.request.fund.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.fund.domain.ProcessedBlockchainEvent;

public interface ProcessedBlockchainEventRepository extends JpaRepository<ProcessedBlockchainEvent, String> {
}
