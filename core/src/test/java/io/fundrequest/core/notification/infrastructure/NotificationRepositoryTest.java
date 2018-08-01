package io.fundrequest.core.notification.infrastructure;

import io.fundrequest.core.infrastructure.AbstractRepositoryTest;
import io.fundrequest.core.notification.domain.Notification;
import io.fundrequest.core.notification.domain.NotificationType;
import io.fundrequest.core.notification.domain.RequestClaimedNotification;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    public void save() {
        RequestClaimedNotification n = createNotification(1L, LocalDateTime.now(), 45L);

        notificationRepository.saveAndFlush(n);

        assertThat(n).isNotNull();
    }

    private RequestClaimedNotification createNotification(long requestId, LocalDateTime date, long blockchainEventId) {
        return new RequestClaimedNotification(NotificationType.REQUEST_CLAIMED, date, blockchainEventId, requestId, "davyvanroy");
    }

    @Test
    public void findLast() {
        for (int i = 0; i < 20; i++) {
            notificationRepository.save(createNotification((long) i, LocalDateTime.now().plusDays(i), 45L));
        }
        notificationRepository.flush();

        List<Notification> result = notificationRepository.findFirst10ByOrderByDateDesc();

        assertThat(result).hasSize(10);
        assertThat(result).isSortedAccordingTo((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
    }
}