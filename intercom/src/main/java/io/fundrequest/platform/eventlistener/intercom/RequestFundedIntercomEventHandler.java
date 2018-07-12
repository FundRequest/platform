package io.fundrequest.platform.eventlistener.intercom;

import io.fundrequest.notification.dto.RequestFundedNotificationDto;
import org.springframework.transaction.event.TransactionalEventListener;

public class RequestFundedIntercomEventHandler {

    @TransactionalEventListener
    public void handle(final RequestFundedNotificationDto event) {

    }
}
