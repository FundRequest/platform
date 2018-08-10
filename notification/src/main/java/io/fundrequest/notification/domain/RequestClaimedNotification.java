package io.fundrequest.notification.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalDateTime;

import static io.fundrequest.notification.dto.NotificationType.REQUEST_CLAIMED;

@Entity
@DiscriminatorValue("REQUEST_CLAIMED")
@Getter
@EqualsAndHashCode(callSuper = true)
public class RequestClaimedNotification extends Notification {

    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "claim_id")
    private Long claimId;

    protected RequestClaimedNotification() {
    }

    public RequestClaimedNotification(final String uuid, final LocalDateTime date, final Long blockchainEventId, final Long requestId, final Long claimId) {
        super(uuid, REQUEST_CLAIMED, date, blockchainEventId);
        this.requestId = requestId;
        this.claimId = claimId;
    }
}
