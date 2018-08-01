package io.fundrequest.core.notification.domain;

import java.time.LocalDateTime;

public final class RequestClaimedNotificationMother {

    public static RequestClaimedNotification aRequestClaimedNotification() {
        return new RequestClaimedNotification(NotificationType.REQUEST_CLAIMED, LocalDateTime.now().minusDays(2), 546L, 1L, "davyvanroy");
    }
}