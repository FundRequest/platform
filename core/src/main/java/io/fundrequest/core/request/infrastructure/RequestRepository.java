package io.fundrequest.core.request.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.domain.Request;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Integer> {
    @Query("SELECT r FROM Request r where r.issueInformation.link = ?1")
    Optional<Request> findByIssueLink(String link);
}
