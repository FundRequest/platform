package io.fundrequest.core.notification.domain;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("REQUEST_FUNDED")
public class RequestFundedNotification extends Notification {

    protected RequestFundedNotification() {
    }

    public RequestFundedNotification(NotificationType type, LocalDateTime date, String transactionId, Long fundId) {
        super(type, date, transactionId);
        this.fundId = fundId;
    }

    @Column(name = "fund_id")
    private Long fundId;

    public Long getFundId() {
        return fundId;
    }
}
