package io.fundrequest.notification.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public abstract class NotificationDto {

    private final String uuid;
    private final NotificationType type;
    private final Long blockchainEventId;
    private final LocalDateTime date;
}
