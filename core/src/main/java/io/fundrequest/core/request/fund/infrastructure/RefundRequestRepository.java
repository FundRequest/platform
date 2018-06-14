package io.fundrequest.core.request.fund.infrastructure;

import io.fundrequest.core.infrastructure.repository.JpaRepository;
import io.fundrequest.core.request.fund.domain.RefundRequest;
import io.fundrequest.core.request.fund.domain.RefundRequestStatus;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface RefundRequestRepository extends JpaRepository<RefundRequest, Long> {

    List<RefundRequest> findAllByStatusIn(List<RefundRequestStatus> status, Sort sort);

    List<RefundRequest> findAllByRequestIdAndStatus(long requestId, RefundRequestStatus status);

    List<RefundRequest> findAllByRequestIdAndFunderAddressAndStatus(long requestId, String funderAddress, RefundRequestStatus status);
}
