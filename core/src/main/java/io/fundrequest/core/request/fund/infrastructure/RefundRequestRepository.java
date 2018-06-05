package io.fundrequest.core.request.fund.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.fund.domain.RefundRequest;

public interface RefundRequestRepository extends JpaRepository<RefundRequest, Long> {
}
