package io.fundrequest.core.notification.domain;

import java.time.LocalDateTime;

public final class RequestCreatedNotificationMother {

    public static RequestCreatedNotification aRequestCreatedNotification() {
        return new RequestCreatedNotification(NotificationType.REQUEST_CREATED, LocalDateTime.now().minusDays(2), 1L);
    }
}