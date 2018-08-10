package io.fundrequest.notification.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.fundrequest.notification.dto.NotificationType.REQUEST_CLAIMED;

@Value
@EqualsAndHashCode(callSuper = true)
public class RequestClaimedNotificationDto extends NotificationDto {

    private final Long requestId;
    private final Long claimId;

    @Builder
    public RequestClaimedNotificationDto(final Long blockchainEventId, final LocalDateTime date, final Long requestId, final Long claimId) {
        super(UUID.randomUUID().toString(), REQUEST_CLAIMED, blockchainEventId, date);
        this.requestId = requestId;
        this.claimId = claimId;
    }
}
