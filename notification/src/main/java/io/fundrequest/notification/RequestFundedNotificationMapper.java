package io.fundrequest.notification;

import io.fundrequest.common.infrastructure.mapping.BaseMapper;
import io.fundrequest.notification.domain.RequestFundedNotification;
import io.fundrequest.notification.dto.RequestFundedNotificationDto;
import org.springframework.stereotype.Component;

@Component
public class RequestFundedNotificationMapper implements BaseMapper<RequestFundedNotificationDto, RequestFundedNotification> {

    @Override
    public RequestFundedNotification map(final RequestFundedNotificationDto notificationDto) {
        return new RequestFundedNotification(notificationDto.getUuid(), notificationDto.getBlockchainEventId(), notificationDto.getDate(), notificationDto.getRequestId(), notificationDto.getFundId());
    }
}
