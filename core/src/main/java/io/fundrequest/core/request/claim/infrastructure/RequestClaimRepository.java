package io.fundrequest.core.request.claim.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.claim.domain.RequestClaim;

public interface RequestClaimRepository extends JpaRepository<RequestClaim, Long> {
}
