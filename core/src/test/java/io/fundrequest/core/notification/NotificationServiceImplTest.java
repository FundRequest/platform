package io.fundrequest.core.notification;

import io.fundrequest.core.notification.domain.Notification;
import io.fundrequest.core.notification.domain.NotificationType;
import io.fundrequest.core.notification.domain.RequestCreatedNotification;
import io.fundrequest.core.notification.domain.RequestCreatedNotificationMother;
import io.fundrequest.core.notification.domain.RequestFundedNotification;
import io.fundrequest.core.notification.domain.RequestFundedNotificationMother;
import io.fundrequest.core.notification.dto.NotificationDto;
import io.fundrequest.core.notification.dto.RequestCreatedNotificationDto;
import io.fundrequest.core.notification.dto.RequestFundedNotificationDto;
import io.fundrequest.core.notification.infrastructure.NotificationRepository;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.event.RequestCreatedEvent;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.view.FundDtoMother;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotificationServiceImplTest {

    private NotificationServiceImpl notificationService;
    private NotificationRepository notificationRepository;
    private ApplicationEventPublisher eventPublisher;
    private RequestService requestService;
    private FundService fundService;

    @Before
    public void setUp() throws Exception {
        notificationRepository = mock(NotificationRepository.class);
        when(notificationRepository.save(any(Notification.class))).then(returnsFirstArg());
        when(notificationRepository.saveAndFlush(any(Notification.class))).then(returnsFirstArg());
        eventPublisher = mock(ApplicationEventPublisher.class);
        requestService = mock(RequestService.class);
        fundService = mock(FundService.class);
        notificationService = new NotificationServiceImpl(
                notificationRepository,
                eventPublisher,
                requestService,
                fundService
        );
    }

    @Test
    public void onRequestCreatedCreatesNotification() throws Exception {
        RequestDto requestDto = RequestDtoMother.freeCodeCampNoUserStories();

        notificationService.onRequestCreated(new RequestCreatedEvent(requestDto));

        assertRequestCreatedNotificationSaved(requestDto);
    }

    private void assertRequestCreatedNotificationSaved(RequestDto requestDto) {
        ArgumentCaptor<RequestCreatedNotification> requestCreatedNotificationArgumentCaptor = ArgumentCaptor.forClass(RequestCreatedNotification.class);
        verify(notificationRepository).save(requestCreatedNotificationArgumentCaptor.capture());
        assertThat(requestCreatedNotificationArgumentCaptor.getValue().getRequestId()).isEqualTo(requestDto.getId());
        assertThat(requestCreatedNotificationArgumentCaptor.getValue().getDate()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(requestCreatedNotificationArgumentCaptor.getValue().getType()).isEqualTo(NotificationType.REQUEST_CREATED);
    }

    @Test
    public void onRequestCreatedPublishesNotification() throws Exception {
        RequestDto requestDto = RequestDtoMother.freeCodeCampNoUserStories();

        notificationService.onRequestCreated(new RequestCreatedEvent(requestDto));

        assertRequestCreatedNotificationPublished(requestDto);
    }

    private void assertRequestCreatedNotificationPublished(RequestDto requestDto) {
        ArgumentCaptor<RequestCreatedNotificationDto> captor = ArgumentCaptor.forClass(RequestCreatedNotificationDto.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue().getRequestDto()).isEqualTo(requestDto);
        assertThat(captor.getValue().getDate()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(captor.getValue().getType()).isEqualTo(NotificationType.REQUEST_CREATED);
    }

    @Test
    public void onRequestFundedCreatesNotification() throws Exception {
        RequestDto requestDto = RequestDtoMother.freeCodeCampNoUserStories();
        FundDto fundDto = FundDtoMother.aFundDto();

        notificationService.onFunded(new RequestFundedEvent(fundDto, requestDto));

        assertRequestFundedNotificationSaved(fundDto);
    }

    private void assertRequestFundedNotificationSaved(FundDto fundDto) {
        ArgumentCaptor<RequestFundedNotification> captor = ArgumentCaptor.forClass(RequestFundedNotification.class);
        verify(notificationRepository).saveAndFlush(captor.capture());
        assertThat(captor.getValue().getFundId()).isEqualTo(fundDto.getId());
        assertThat(captor.getValue().getDate()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(captor.getValue().getType()).isEqualTo(NotificationType.REQUEST_FUNDED);
    }

    @Test
    public void onRequestFundedPublishesNotification() throws Exception {
        RequestDto requestDto = RequestDtoMother.freeCodeCampNoUserStories();
        FundDto fundDto = FundDtoMother.aFundDto();

        notificationService.onFunded(new RequestFundedEvent(fundDto, requestDto));

        assertRequestFundedNotificationPublished(fundDto, requestDto);
    }

    private void assertRequestFundedNotificationPublished(FundDto fundDto, RequestDto requestDto) {
        ArgumentCaptor<RequestFundedNotificationDto> captor = ArgumentCaptor.forClass(RequestFundedNotificationDto.class);
        verify(eventPublisher).publishEvent(captor.capture());
        assertThat(captor.getValue().getFundDto()).isEqualTo(fundDto);
        assertThat(captor.getValue().getRequestDto()).isEqualTo(requestDto);
        assertThat(captor.getValue().getDate()).isEqualToIgnoringSeconds(LocalDateTime.now());
        assertThat(captor.getValue().getType()).isEqualTo(NotificationType.REQUEST_FUNDED);
    }

    @Test
    public void getLast() throws Exception {
        RequestCreatedNotification requestCreatedNotification = RequestCreatedNotificationMother.aRequestCreatedNotification();
        RequestDto requestDtoForFund = RequestDtoMother.freeCodeCampNoUserStories();
        FundDto fundDto = FundDtoMother.aFundDto();
        RequestFundedNotification requestFundedNotification = RequestFundedNotificationMother.aRequestFundedNotification();
        fundDto.setId(requestFundedNotification.getFundId())
        ;
        List<Notification> notifications = Arrays.asList(
                requestCreatedNotification,
                requestFundedNotification
        );
        when(notificationRepository.findFirst10ByOrderByDateDesc()).thenReturn(notifications);
        RequestDto requestDto = RequestDtoMother.freeCodeCampNoUserStories();
        when(requestService.findAll(Collections.singleton(requestCreatedNotification.getRequestId())))
                .thenReturn(Collections.singletonList(requestDto));


        when(fundService.findAll(Collections.singleton(requestFundedNotification.getFundId())))
                .thenReturn(Collections.singletonList(fundDto));
        when(requestService.findAll(Collections.singleton(fundDto.getRequestId())))
                .thenReturn(Collections.singletonList(requestDtoForFund));

        List<NotificationDto> lastNotifications = notificationService.getLastNotifications();

        assertThat(lastNotifications).hasSize(2);
    }
}