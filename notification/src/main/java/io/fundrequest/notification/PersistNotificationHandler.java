package io.fundrequest.notification;

import io.fundrequest.notification.dto.RequestClaimedNotificationDto;
import io.fundrequest.notification.dto.RequestFundedNotificationDto;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Component
public class PersistNotificationHandler {

    private final NotificationRepository notificationRepository;
    private final RequestFundedNotificationMapper requestFundedNotificationMapper;
    private final RequestClaimedNotificationMapper requestClaimedNotificationMapper;

    public PersistNotificationHandler(final NotificationRepository notificationRepository,
                                      final RequestFundedNotificationMapper requestFundedNotificationMapper,
                                      final RequestClaimedNotificationMapper requestClaimedNotificationMapper) {
        this.notificationRepository = notificationRepository;
        this.requestFundedNotificationMapper = requestFundedNotificationMapper;
        this.requestClaimedNotificationMapper = requestClaimedNotificationMapper;
    }

    @EventListener
    @Transactional(propagation = REQUIRES_NEW)
    public void handle(final RequestFundedNotificationDto notification) {
        notificationRepository.save(requestFundedNotificationMapper.map(notification));
    }

    @EventListener
    @Transactional(propagation = REQUIRES_NEW)
    public void handle(final RequestClaimedNotificationDto notification) {
        notificationRepository.save(requestClaimedNotificationMapper.map(notification));
    }
}
