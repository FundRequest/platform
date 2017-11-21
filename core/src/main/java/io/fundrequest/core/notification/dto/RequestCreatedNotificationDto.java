package io.fundrequest.core.notification.dto;

import io.fundrequest.core.notification.domain.NotificationType;
import io.fundrequest.core.request.view.RequestDto;

import java.time.LocalDateTime;

public class RequestCreatedNotificationDto extends NotificationDto {

    private RequestDto requestDto;

    public RequestCreatedNotificationDto(NotificationType type, LocalDateTime date, RequestDto requestDto) {
        super(type, date);
        this.requestDto = requestDto;
    }

    public RequestDto getRequestDto() {
        return requestDto;
    }
}
