package io.fundrequest.core.fund.repository;

import io.fundrequest.core.fund.domain.PendingFund;
import io.fundrequest.core.infrastructure.repository.JpaRepository;

public interface PendingFundRepository extends JpaRepository<PendingFund, Long> {

}
