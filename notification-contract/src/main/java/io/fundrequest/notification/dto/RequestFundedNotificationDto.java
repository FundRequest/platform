package io.fundrequest.notification.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.fundrequest.notification.dto.NotificationType.REQUEST_FUNDED;

@Value
@EqualsAndHashCode(callSuper = true)
public class RequestFundedNotificationDto extends NotificationDto {

    private Long requestId;
    private Long fundId;

    @Builder
    public RequestFundedNotificationDto(final Long blockchainEventId, final LocalDateTime date, final Long requestId, final Long fundId) {
        super(UUID.randomUUID().toString(), REQUEST_FUNDED, blockchainEventId, date);
        this.requestId = requestId;
        this.fundId = fundId;
    }
}
