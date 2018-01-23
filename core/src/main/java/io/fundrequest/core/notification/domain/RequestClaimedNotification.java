package io.fundrequest.core.notification.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("REQUEST_CLAIMED")
public class RequestClaimedNotification extends Notification {

    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "solver")
    private String solver;

    protected RequestClaimedNotification() {
    }

    public RequestClaimedNotification(NotificationType type, LocalDateTime date, String transactionId, Long requestId, String solver) {
        super(type, date, transactionId);
        this.requestId = requestId;
        this.solver = solver;
    }

    public Long getRequestId() {
        return requestId;
    }

    public String getSolver() {
        return solver;
    }
}
