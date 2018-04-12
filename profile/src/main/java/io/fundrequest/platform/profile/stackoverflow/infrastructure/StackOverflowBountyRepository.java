package io.fundrequest.platform.profile.stackoverflow.infrastructure;

import io.fundrequest.platform.profile.stackoverflow.domain.StackOverflowBounty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StackOverflowBountyRepository extends JpaRepository<StackOverflowBounty, Long> {

    Optional<StackOverflowBounty> findByUserId(String userId);

    boolean existsByUserId(String userId);
}
