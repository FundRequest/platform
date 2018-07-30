package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.notification.dto.RequestFundedNotificationDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class PublishRequestFundedNotificationHandler {

    private final ApplicationEventPublisher eventPublisher;

    public PublishRequestFundedNotificationHandler(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @TransactionalEventListener
    public void onFunded(final RequestFundedEvent fundedEvent) {
        eventPublisher.publishEvent(new RequestFundedNotificationDto(fundedEvent.getFundDto().getBlockchainEventId(),
                                                                     fundedEvent.getTimestamp(),
                                                                     fundedEvent.getRequestId(),
                                                                     fundedEvent.getFundDto().getId()));
    }

}
