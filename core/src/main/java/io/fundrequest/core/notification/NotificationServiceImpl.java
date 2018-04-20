package io.fundrequest.core.notification;

import io.fundrequest.core.notification.domain.Notification;
import io.fundrequest.core.notification.domain.NotificationType;
import io.fundrequest.core.notification.domain.RequestClaimedNotification;
import io.fundrequest.core.notification.domain.RequestFundedNotification;
import io.fundrequest.core.notification.dto.NotificationDto;
import io.fundrequest.core.notification.dto.RequestClaimedNotificationDto;
import io.fundrequest.core.notification.dto.RequestFundedNotificationDto;
import io.fundrequest.core.notification.infrastructure.NotificationRepository;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.event.RequestClaimedEvent;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.view.RequestDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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


    public NotificationServiceImpl(NotificationRepository notificationRepository,
                                   ApplicationEventPublisher eventPublisher,
                                   RequestService requestService,
                                   FundService fundService) {
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
        result.addAll(getRequestFundedNotifications(notifications));
        result.addAll(getRequestClaimedNotifications(notifications));
        result.sort((o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        return result;
    }

    private List<NotificationDto> getRequestClaimedNotifications(List<Notification> notifications) {
        List<RequestClaimedNotification> requestCreatedNotifications = notifications
                .stream()
                .filter(n -> n.getType() == NotificationType.REQUEST_CLAIMED)
                .map(n -> (RequestClaimedNotification) n)
                .collect(Collectors.toList());

        Map<Long, RequestDto> requestMap =
                requestService.findAll(requestCreatedNotifications.stream().map(RequestClaimedNotification::getRequestId).collect(Collectors.toSet()))
                              .stream().collect(Collectors.toMap(RequestDto::getId, n -> n));

        return requestCreatedNotifications.stream()
                                          .map(n -> new RequestClaimedNotificationDto(n.getId(),
                                                                                      n.getTransactionId(),
                                                                                      n.getDate(),
                                                                                      requestMap.get(n.getRequestId()),
                                                                                      n.getSolver()))
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
                                                 n.getTransactionId(),
                                                 n.getDate(),
                                                 requestMap.get(fundedMap.get(n.getFundId()).getRequestId()),
                                                 fundedMap.get(n.getFundId())))
                                         .collect(Collectors.toList());
    }


    @EventListener
    @Transactional
    public void onFunded(RequestFundedEvent fundedEvent) {
        RequestFundedNotification notification = new RequestFundedNotification(NotificationType.REQUEST_FUNDED,
                                                                               fundedEvent.getTimestamp(),
                                                                               fundedEvent.getTransactionId(),
                                                                               fundedEvent.getFundDto().getId());
        notification = notificationRepository.saveAndFlush(notification);
        publishNotification(createRequestFundedNotification(notification, fundedEvent.getRequestDto(), fundedEvent.getFundDto()));
    }

    private RequestFundedNotificationDto createRequestFundedNotification(RequestFundedNotification notification, RequestDto requestDto, FundDto fundDto) {
        return new RequestFundedNotificationDto(
                notification.getId(),
                notification.getTransactionId(),
                notification.getDate(),
                requestDto,
                fundDto
        );
    }

    @EventListener
    @Transactional
    public void onClaimed(RequestClaimedEvent claimedEvent) {
        RequestClaimedNotification notification = new RequestClaimedNotification(NotificationType.REQUEST_CLAIMED,
                                                                                 claimedEvent.getTimestamp(),
                                                                                 claimedEvent.getTransactionId(),
                                                                                 claimedEvent.getRequestDto().getId(),
                                                                                 claimedEvent.getSolver());
        notification = notificationRepository.saveAndFlush(notification);
        publishNotification(
                createRequestClaimedNotification(notification.getId(), claimedEvent)
                           );
    }

    private RequestClaimedNotificationDto createRequestClaimedNotification(Long id, RequestClaimedEvent claimedEvent) {
        return new RequestClaimedNotificationDto(id, claimedEvent.getTransactionId(), claimedEvent.getTimestamp(), claimedEvent.getRequestDto(), claimedEvent.getSolver());
    }

    private void publishNotification(NotificationDto notification) {
        eventPublisher.publishEvent(notification);
    }
}
