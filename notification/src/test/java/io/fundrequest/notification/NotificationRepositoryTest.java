package io.fundrequest.notification;

import io.fundrequest.notification.domain.RequestClaimedNotification;
import io.fundrequest.notification.domain.RequestClaimedNotificationMother;
import io.fundrequest.notification.domain.RequestFundedNotification;
import io.fundrequest.notification.domain.RequestFundedNotificationMother;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    public void save_RequestClaimedNotification() {
        RequestClaimedNotification notification = RequestClaimedNotificationMother.aRequestClaimedNotification();

        notificationRepository.saveAndFlush(notification);

        assertThat(notification.getId()).isNotNull();
    }

    @Test
    public void save_RequestFundedNotification() {
        RequestFundedNotification notification = RequestFundedNotificationMother.aRequestFundedNotification();

        notificationRepository.saveAndFlush(notification);

        assertThat(notification.getId()).isNotNull();
    }
}