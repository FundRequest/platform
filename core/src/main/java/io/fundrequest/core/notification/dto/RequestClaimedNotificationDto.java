package io.fundrequest.core.notification.dto;

import io.fundrequest.core.notification.domain.NotificationType;
import io.fundrequest.core.request.view.RequestDto;

import java.time.LocalDateTime;

public class RequestClaimedNotificationDto extends NotificationDto {

    private RequestDto requestDto;
    private String solver;

    public RequestClaimedNotificationDto(Long id, String transactionId, LocalDateTime date, RequestDto requestDto, String solver) {
        super(id, NotificationType.REQUEST_CLAIMED, transactionId, date);
        this.requestDto = requestDto;
        this.solver = solver;
    }

    public RequestDto getRequestDto() {
        return requestDto;
    }

    public String getSolver() {
        return solver;
    }
}
