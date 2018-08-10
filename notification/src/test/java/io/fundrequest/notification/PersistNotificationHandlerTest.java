package io.fundrequest.notification;

import io.fundrequest.notification.domain.RequestClaimedNotification;
import io.fundrequest.notification.domain.RequestClaimedNotificationMother;
import io.fundrequest.notification.domain.RequestFundedNotification;
import io.fundrequest.notification.domain.RequestFundedNotificationMother;
import io.fundrequest.notification.dto.RequestClaimedNotificationDto;
import io.fundrequest.notification.dto.RequestFundedNotificationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Matchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersistNotificationHandlerTest {

    private PersistNotificationHandler handler;
    private NotificationRepository notificationRepository;
    private RequestFundedNotificationMapper requestFundedNotificationMapper;
    private RequestClaimedNotificationMapper requestClaimedNotificationMapper;

    @BeforeEach
    void setUp() {
        notificationRepository = mock(NotificationRepository.class);
        requestFundedNotificationMapper = mock(RequestFundedNotificationMapper.class);
        requestClaimedNotificationMapper = mock(RequestClaimedNotificationMapper.class);
        handler = new PersistNotificationHandler(notificationRepository, requestFundedNotificationMapper, requestClaimedNotificationMapper);
    }

    @Test
    void handleRequestFunded() {
        final RequestFundedNotificationDto notificationDto = RequestFundedNotificationDto.builder().build();
        final RequestFundedNotification notification = RequestFundedNotificationMother.aRequestFundedNotification();

        when(requestFundedNotificationMapper.map(same(notificationDto))).thenReturn(notification);

        handler.handle(notificationDto);

        verify(notificationRepository).save(same(notification));
    }

    @Test
    void handleRequestClaimed() {
        final RequestClaimedNotificationDto notificationDto = RequestClaimedNotificationDto.builder().build();
        final RequestClaimedNotification notification = RequestClaimedNotificationMother.aRequestClaimedNotification();

        when(requestClaimedNotificationMapper.map(same(notificationDto))).thenReturn(notification);

        handler.handle(notificationDto);

        verify(notificationRepository).save(same(notification));
    }
}
