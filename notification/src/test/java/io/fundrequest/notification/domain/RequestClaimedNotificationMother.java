package io.fundrequest.notification.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public final class RequestClaimedNotificationMother {

    private RequestClaimedNotificationMother() {}

    public static RequestClaimedNotification aRequestClaimedNotification() {
        return new RequestClaimedNotification(UUID.randomUUID().toString(), LocalDateTime.now().minusDays(2), 546L, 1L, 245L);
    }
}