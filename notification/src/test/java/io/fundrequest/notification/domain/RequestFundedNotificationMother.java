package io.fundrequest.notification.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public final class RequestFundedNotificationMother {

    private RequestFundedNotificationMother() { }

    public static RequestFundedNotification aRequestFundedNotification() {
        return new RequestFundedNotification(UUID.randomUUID().toString(), 547L, LocalDateTime.now().minusDays(2), 858L, 36747L);
    }

}