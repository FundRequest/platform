package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.command.UpdateRequestStatusCommand;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.RefundProcessedEvent;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.token.dto.TokenValueDto;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static io.fundrequest.core.request.domain.RequestStatus.FUNDED;
import static io.fundrequest.core.request.domain.RequestStatus.OPEN;
import static java.math.BigDecimal.ZERO;

@Component
public class CheckFundsGtZeroHandler {

    private final FundService fundService;
    private final RequestService requestService;

    public CheckFundsGtZeroHandler(final FundService fundService, final RequestService requestService) {
        this.fundService = fundService;
        this.requestService = requestService;
    }

    @EventListener
    public void handleFund(final RequestFundedEvent requestFundedEvent) {
        final Long requestId = requestFundedEvent.getRequestId();

        if (hasTokenValueGtZero(fundService.getTotalFundsForRequest(requestId)) && hasRequestStatus(requestId, OPEN)) {
            requestService.update(new UpdateRequestStatusCommand(requestId, FUNDED));
        }
    }

    @EventListener
    public void handleRefund(final RefundProcessedEvent refundProcessedEvent) {
        final Long requestId = refundProcessedEvent.getRefund().getRequestId();

        if (!hasTokenValueGtZero(fundService.getTotalFundsForRequest(requestId)) && hasRequestStatus(requestId, FUNDED)) {
            requestService.update(new UpdateRequestStatusCommand(requestId, OPEN));
        }
    }

    private boolean hasTokenValueGtZero(List<TokenValueDto> totalFundsForRequest) {
        return totalFundsForRequest.stream().anyMatch(tokenValue -> tokenValue.getTotalAmount().compareTo(ZERO) > 0);
    }

    private boolean hasRequestStatus(final Long requestId, final RequestStatus status) {
        return status == requestService.findRequest(requestId).getStatus();
    }
}
