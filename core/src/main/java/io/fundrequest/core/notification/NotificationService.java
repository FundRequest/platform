package io.fundrequest.core.notification;

import io.fundrequest.core.notification.dto.NotificationDto;

import java.util.List;

public interface NotificationService {
    List<NotificationDto> getLastNotifications();
}
