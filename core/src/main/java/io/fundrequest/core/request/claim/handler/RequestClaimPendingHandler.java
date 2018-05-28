package io.fundrequest.core.request.claim.handler;

import io.fundrequest.core.request.claim.event.ClaimRequestedEvent;
import io.fundrequest.core.telegram.TelegramService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@ConditionalOnBean(TelegramService.class)
public class RequestClaimPendingHandler {

    private TelegramService telegramService;

    public RequestClaimPendingHandler(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRequestClaimPending(final ClaimRequestedEvent claimRequestedEvent) {
        telegramService.sendMessageToChannel("Holy Moly, "
                                             + claimRequestedEvent.getRequestClaim().getSolver()
                                             + " just requested an approval for a claim! ("
                                             + claimRequestedEvent.getRequestClaim().getRequestId()
                                             + ")");
    }
}
