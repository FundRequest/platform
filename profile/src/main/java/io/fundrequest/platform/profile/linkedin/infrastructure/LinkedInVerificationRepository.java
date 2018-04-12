package io.fundrequest.platform.profile.linkedin.infrastructure;

import io.fundrequest.platform.profile.linkedin.domain.LinkedInVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LinkedInVerificationRepository extends JpaRepository<LinkedInVerification, Long> {
    Optional<LinkedInVerification> findByUserId(String userId);
}
