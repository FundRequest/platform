package io.fundrequest.core.request.fund.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.fund.domain.PendingFund;

import java.util.List;
import java.util.Optional;

public interface PendingFundRepository extends JpaRepository<PendingFund, Long> {

    Optional<PendingFund> findByTransactionHash(String transactionHash);

    List<PendingFund> findByUserId(String userId);
}
