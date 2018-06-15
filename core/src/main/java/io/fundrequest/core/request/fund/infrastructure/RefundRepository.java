package io.fundrequest.core.request.fund.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.fund.domain.Refund;

import java.util.List;

public interface RefundRepository extends JpaRepository<Refund, Long> {

    List<Refund> findAllByRequestId(long requestId);
}
