package io.fundrequest.core.notification;

import io.fundrequest.core.notification.domain.Notification;
import io.fundrequest.core.notification.domain.NotificationType;
import io.fundrequest.core.notification.domain.RequestCreatedNotification;
import io.fundrequest.core.notification.domain.RequestFundedNotification;
import io.fundrequest.core.notification.dto.NotificationDto;
import io.fundrequest.core.notification.dto.RequestCreatedNotificationDto;
import io.fundrequest.core.notification.dto.RequestFundedNotificationDto;
import io.fundrequest.core.notification.infrastructure.NotificationRepository;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.event.RequestCreatedEvent;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.view.RequestDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;
    private ApplicationEventPublisher eventPublisher;
    private RequestService requestService;
    private FundService fundService;


    public NotificationServiceImpl(NotificationRepository notificationRepository, ApplicationEventPublisher eventPublisher, RequestService requestService, FundService fundService) {
        this.notificationRepository = notificationRepository;
        this.eventPublisher = eventPublisher;
        this.requestService = requestService;
        this.fundService = fundService;
    }

    @Transactional(readOnly = true)
    @Override
    public List<NotificationDto> getLastNotifications() {
        List<Notification> notifications = notificationRepository.findFirst10ByOrderByDateDesc();
        List<NotificationDto> result = new ArrayList<>();
        result.addAll(getRequestCreatedNotifications(notifications));
        result.addAll(getRequestFundedNotifications(notifications));
        result.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        return result;
    }

    private List<NotificationDto> getRequestCreatedNotifications(List<Notification> notifications) {
        List<RequestCreatedNotification> requestCreatedNotifications = notifications
                .stream()
                .filter(n -> n.getType() == NotificationType.REQUEST_CREATED)
                .map(n -> (RequestCreatedNotification) n)
                .collect(Collectors.toList());

        Map<Long, RequestDto> requestMap =
                requestService.findAll(requestCreatedNotifications.stream().map(RequestCreatedNotification::getRequestId).collect(Collectors.toSet()))
                        .stream().collect(Collectors.toMap(RequestDto::getId, n -> n));

        return requestCreatedNotifications.stream()
                .map(n -> new RequestCreatedNotificationDto(n.getId(), NotificationType.REQUEST_CREATED, n.getDate(), requestMap.get(n.getRequestId())))
                .collect(Collectors.toList());
    }

    private List<NotificationDto> getRequestFundedNotifications(List<Notification> notifications) {
        List<RequestFundedNotification> requestFundedNotifications = notifications
                .stream()
                .filter(n -> n.getType() == NotificationType.REQUEST_FUNDED)
                .map(n -> (RequestFundedNotification) n)
                .collect(Collectors.toList());

        Map<Long, FundDto> fundedMap =
                fundService.findAll(requestFundedNotifications.stream().map(RequestFundedNotification::getFundId).collect(Collectors.toSet()))
                        .stream().collect(Collectors.toMap(FundDto::getId, n -> n));

        Map<Long, RequestDto> requestMap =
                requestService.findAll(fundedMap.values()
                        .stream().map(FundDto::getRequestId).collect(Collectors.toSet()))
                        .stream().collect(Collectors.toMap(RequestDto::getId, n -> n));

        return requestFundedNotifications.stream()
                .map(n -> new RequestFundedNotificationDto(
                        n.getId(),
                        NotificationType.REQUEST_FUNDED,
                        n.getDate(),
                        requestMap.get(fundedMap.get(n.getFundId()).getRequestId()),
                        fundedMap.get(n.getFundId())))
                .collect(Collectors.toList());
    }

    @EventListener
    @Transactional
    public void onRequestCreated(RequestCreatedEvent requestCreatedEvent) {
        RequestCreatedNotification notification = new RequestCreatedNotification(NotificationType.REQUEST_CREATED, LocalDateTime.now(), requestCreatedEvent.getRequestDto().getId());
        notification = notificationRepository.save(notification);
        publishNotification(
                createRequestCreatedNotification(notification.getId(), requestCreatedEvent.getRequestDto())
        );
    }

    private RequestCreatedNotificationDto createRequestCreatedNotification(Long id, RequestDto requestDto) {
        return new RequestCreatedNotificationDto(id, NotificationType.REQUEST_CREATED, LocalDateTime.now(), requestDto);
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    @Transactional
    public void onFunded(RequestFundedEvent fundedEvent) {
        RequestFundedNotification notification = new RequestFundedNotification(NotificationType.REQUEST_FUNDED, LocalDateTime.now(), fundedEvent.getFundDto().getId());
        notification = notificationRepository.save(notification);
        publishNotification(createRequestFundedNotification(notification, fundedEvent.getRequestDto(), fundedEvent.getFundDto()));
    }

    private RequestFundedNotificationDto createRequestFundedNotification(RequestFundedNotification notification, RequestDto requestDto, FundDto fundDto) {
        return new RequestFundedNotificationDto(
                notification.getId(),
                NotificationType.REQUEST_FUNDED,
                notification.getDate(),
                requestDto,
                fundDto
        );
    }

    private void publishNotification(NotificationDto notification) {
        eventPublisher.publishEvent(notification);
    }
}
