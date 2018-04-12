package io.fundrequest.platform.profile.linkedin.infrastructure;

import io.fundrequest.platform.profile.linkedin.domain.LinkedInPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkedInPostRepository extends JpaRepository<LinkedInPost, Long> {
}
