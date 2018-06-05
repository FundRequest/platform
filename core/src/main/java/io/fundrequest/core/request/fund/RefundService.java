package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.command.RequestRefundCommand;

public interface RefundService {

    void requestRefund(RequestRefundCommand requestRefundCommand);
}
