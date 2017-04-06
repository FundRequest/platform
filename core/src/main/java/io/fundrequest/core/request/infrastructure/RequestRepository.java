package io.fundrequest.core.request.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.domain.Request;

public interface RequestRepository extends JpaRepository<Request, Integer> {
}
