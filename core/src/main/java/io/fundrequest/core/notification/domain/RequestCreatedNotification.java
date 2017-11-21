package io.fundrequest.core.notification.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("REQUEST_CREATED")
public class RequestCreatedNotification extends Notification {

    @Column(name = "request_id")
    private Long requestId;

    protected RequestCreatedNotification() {
    }

    public RequestCreatedNotification(NotificationType type, LocalDateTime date, Long requestId) {
        super(type, date);
        this.requestId = requestId;
    }

    public Long getRequestId() {
        return requestId;
    }
}
