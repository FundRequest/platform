package io.fundrequest.core.notification.infrastructure;

import io.fundrequest.core.infrastructure.AbstractRepositoryTest;
import io.fundrequest.core.notification.domain.Notification;
import io.fundrequest.core.notification.domain.NotificationType;
import io.fundrequest.core.notification.domain.RequestCreatedNotification;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    public void save() throws Exception {
        RequestCreatedNotification n = createNotification(1L, LocalDateTime.now());

        notificationRepository.saveAndFlush(n);

        assertThat(n).isNotNull();
    }

    private RequestCreatedNotification createNotification(long requestId, LocalDateTime date) {
        return new RequestCreatedNotification(NotificationType.REQUEST_CREATED, date, requestId);
    }

    @Test
    public void findLast() throws Exception {
        for (int i = 0; i < 20; i++) {
            notificationRepository.save(createNotification((long) i, LocalDateTime.now().plusDays(i)));
        }
        notificationRepository.flush();

        List<Notification> result = notificationRepository.findFirst10ByOrderByDateDesc();

        assertThat(result).hasSize(10);
        assertThat(result).isSortedAccordingTo((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
    }
}