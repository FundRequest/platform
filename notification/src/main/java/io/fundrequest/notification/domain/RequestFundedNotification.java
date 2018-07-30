package io.fundrequest.notification.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

import static io.fundrequest.notification.dto.NotificationType.REQUEST_FUNDED;

@Entity
@DiscriminatorValue("REQUEST_FUNDED")
@Getter
@EqualsAndHashCode(callSuper = true)
public class RequestFundedNotification extends Notification {

    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "fund_id")
    private Long fundId;


    protected RequestFundedNotification() {
    }

    @Builder
    public RequestFundedNotification(final String uuid, final Long blockchainEventId, final LocalDateTime date, final Long requestId, final Long fundId) {
        super(uuid, REQUEST_FUNDED, date, blockchainEventId);
        this.requestId = requestId;
        this.fundId = fundId;
    }
}
