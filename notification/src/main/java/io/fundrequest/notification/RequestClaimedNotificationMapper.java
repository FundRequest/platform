package io.fundrequest.notification;

import io.fundrequest.common.infrastructure.mapping.BaseMapper;
import io.fundrequest.notification.domain.RequestClaimedNotification;
import io.fundrequest.notification.dto.RequestClaimedNotificationDto;
import org.springframework.stereotype.Component;

@Component
public class RequestClaimedNotificationMapper implements BaseMapper<RequestClaimedNotificationDto, RequestClaimedNotification> {

    @Override
    public RequestClaimedNotification map(final RequestClaimedNotificationDto notificationDto) {
        return new RequestClaimedNotification(notificationDto.getUuid(), notificationDto.getDate(), notificationDto.getBlockchainEventId(), notificationDto.getRequestId(), notificationDto.getClaimId());
    }
}
