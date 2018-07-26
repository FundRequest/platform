package io.fundrequest.core.request.fund.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.fund.domain.RefundRequest;
import io.fundrequest.core.request.fund.domain.RefundRequestStatus;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface RefundRequestRepository extends JpaRepository<RefundRequest, Long> {

    List<RefundRequest> findAllByStatus(RefundRequestStatus status);

    List<RefundRequest> findAllByStatusIn(List<RefundRequestStatus> status, Sort sort);

    List<RefundRequest> findAllByRequestIdAndStatusIn(long requestId, RefundRequestStatus... statuses);

    List<RefundRequest> findAllByRequestIdAndFunderAddressAndStatus(long requestId, String funderAddress, RefundRequestStatus status);

    Optional<RefundRequest> findByTransactionHash(String transactionHash);
}
