package io.fundrequest.platform.profile.ref.infrastructure;

import io.fundrequest.platform.profile.ref.domain.Referral;
import io.fundrequest.platform.profile.ref.domain.ReferralStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReferralRepository extends JpaRepository<Referral, Long> {
    boolean existsByReferrerAndReferee(String referrer, String referee);

    boolean existsByReferee(String referee);

    List<Referral> findByReferrer(String referrer, Sort sort);

    List<Referral> findByReferrer(String referrer);

    long countByReferrer(String referrer);

    Optional<Referral> findByReferee(String referee);

    Long countByReferrerAndStatus(String referrer, ReferralStatus status);
}
