package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.claim.event.RequestClaimedEvent;
import io.fundrequest.notification.dto.RequestClaimedNotificationDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PublishRequestClaimedNotificationHandler {

    private final ApplicationEventPublisher eventPublisher;

    public PublishRequestClaimedNotificationHandler(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @TransactionalEventListener
    public void onClaimed(final RequestClaimedEvent claimedEvent) {
        eventPublisher.publishEvent(RequestClaimedNotificationDto.builder()
                                                                 .blockchainEventId(claimedEvent.getBlockchainEventId())
                                                                 .date(claimedEvent.getTimestamp())
                                                                 .requestId(claimedEvent.getRequestDto().getId())
                                                                 .claimId(claimedEvent.getClaimDto().getId())
                                                                 .build());
    }
}
