package io.fundrequest.core.request.claim.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.claim.domain.TrustedRepo;

public interface TrustedRepoRepository extends JpaRepository<TrustedRepo, Long> {
}
