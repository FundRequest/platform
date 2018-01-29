package io.fundrequest.core.notification.domain;

import java.time.LocalDateTime;

public final class RequestClaimedNotificationMother {

    public static RequestClaimedNotification aRequestClaimedNotification() {
        return new RequestClaimedNotification(NotificationType.REQUEST_CLAIMED, LocalDateTime.now().minusDays(2), "0x631e464d85a3e72f89b5e510a0807688be42764c99f238e1e1d7c123dcf804f3", 1L, "davyvanroy");
    }
}