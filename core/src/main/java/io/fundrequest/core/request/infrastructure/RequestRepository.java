package io.fundrequest.core.request.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.domain.Request;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    @Query("SELECT r FROM Request r where r.issueInformation.platform = ?1 and r.issueInformation.platformId = ?2")
    Optional<Request> findByPlatformAndPlatformId(Platform platform, String platformId);

    @Query("SELECT distinct r FROM Request r where ?1 member of r.watchers")
    List<Request> findRequestsUserIsWatching(String user);

    @Query("SELECT distinct r FROM Request r, Fund f where f.requestId = r.id and (f.funderUserId = ?1 or f.funder = lower(?2))")
    List<Request> findRequestsUserHasFunded(String user, String userAddress);
}
