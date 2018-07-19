package io.fundrequest.core.notification.infrastructure;

import io.fundrequest.core.infrastructure.AbstractRepositoryTest;
import io.fundrequest.core.notification.domain.Notification;
import io.fundrequest.core.notification.domain.NotificationType;
import io.fundrequest.core.notification.domain.RequestClaimedNotification;
import io.fundrequest.core.request.domain.BlockchainEvent;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private EntityManager entityManager;

    @Test
    public void save() {
        RequestClaimedNotification n = createNotification(LocalDateTime.now());

        notificationRepository.saveAndFlush(n);

        assertThat(n).isNotNull();
    }

    @Test
    public void findLast() {
        for (int i = 0; i < 20; i++) {
            notificationRepository.save(createNotification(LocalDateTime.now().plusDays(i)));
        }
        notificationRepository.flush();

        List<Notification> result = notificationRepository.findFirst10ByOrderByDateDesc();

        assertThat(result).hasSize(10);
        assertThat(result).isSortedAccordingTo((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
    }

    private RequestClaimedNotification createNotification(final LocalDateTime date) {
        final Request request = RequestMother.fundRequestArea51().build();
        final BlockchainEvent blockchainEvent = new BlockchainEvent(UUID.randomUUID().toString(), "1");
        entityManager.persist(request);
        entityManager.persist(blockchainEvent);
        return new RequestClaimedNotification(NotificationType.REQUEST_CLAIMED, date, blockchainEvent.getId(), request.getId(), "davyvanroy");
    }
}