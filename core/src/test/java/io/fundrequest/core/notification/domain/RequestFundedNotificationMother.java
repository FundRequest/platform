package io.fundrequest.core.notification.domain;

import java.time.LocalDateTime;

public final class RequestFundedNotificationMother {

    public static RequestFundedNotification aRequestFundedNotification() {
        return new RequestFundedNotification(NotificationType.REQUEST_FUNDED, LocalDateTime.now().minusDays(2), 36747L);
    }

}