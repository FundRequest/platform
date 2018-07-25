package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.command.RefundProcessedCommand;
import io.fundrequest.core.request.fund.command.RequestRefundCommand;
import io.fundrequest.core.request.fund.domain.RefundRequestStatus;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;

import java.util.List;

public interface RefundService {

    void requestRefund(RequestRefundCommand requestRefundCommand);

    List<RefundRequestDto> findAllRefundRequestsFor(long requestId, RefundRequestStatus... statuses);

    List<RefundRequestDto> findAllRefundRequestsFor(long requestId, String funderAddress, RefundRequestStatus status);

    void refundProcessed(RefundProcessedCommand command);
}
