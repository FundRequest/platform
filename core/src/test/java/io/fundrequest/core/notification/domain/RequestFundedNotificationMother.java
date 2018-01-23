package io.fundrequest.core.notification.domain;

import java.time.LocalDateTime;

public final class RequestFundedNotificationMother {

    public static RequestFundedNotification aRequestFundedNotification() {
        return new RequestFundedNotification(NotificationType.REQUEST_FUNDED, LocalDateTime.now().minusDays(2), "0x8a678217eb8f3fe491ddb368e1e4d8ec6b3ce0f41ff729442eded83bcf0036d6", 36747L);
    }

}