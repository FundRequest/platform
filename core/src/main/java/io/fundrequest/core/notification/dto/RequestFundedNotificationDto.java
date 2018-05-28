package io.fundrequest.core.notification.dto;

import io.fundrequest.core.notification.domain.NotificationType;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.view.RequestDto;

import java.time.LocalDateTime;

public class RequestFundedNotificationDto extends NotificationDto {

    private RequestDto requestDto;
    private FundDto fundDto;

    public RequestFundedNotificationDto(Long id, Long blockchainEventId, LocalDateTime date, RequestDto requestDto, FundDto fundDto) {
        super(id, NotificationType.REQUEST_FUNDED, blockchainEventId, date);
        this.requestDto = requestDto;
        this.fundDto = fundDto;
    }

    public RequestDto getRequestDto() {
        return requestDto;
    }

    public FundDto getFundDto() {
        return fundDto;
    }
}
