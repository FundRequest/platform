package io.fundrequest.core.request.fund.infrastructure;

import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.infrastructure.repository.JpaRepository;

import java.util.List;

public interface FundRepository extends JpaRepository<Fund, Long> {

    List<Fund> findByRequestId(Long requestId);

}
