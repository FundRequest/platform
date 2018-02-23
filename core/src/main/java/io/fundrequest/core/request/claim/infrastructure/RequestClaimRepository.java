package io.fundrequest.core.request.claim.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.claim.domain.ClaimRequestStatus;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface RequestClaimRepository extends JpaRepository<RequestClaim, Long> {

    List<RequestClaim> findByStatusIn(List<ClaimRequestStatus> status, Sort sort);
}
