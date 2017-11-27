package io.fundrequest.restapi.notification;


import io.fundrequest.core.notification.NotificationService;
import io.fundrequest.core.notification.dto.NotificationDto;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class NotificationController {

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(path = "/api/public/notifications-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getNotificationsStream() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        notificationService.getLastNotifications().forEach(n -> {
            try {
                emitter.send(n, MediaType.APPLICATION_JSON);
            } catch (IOException e) {
                emitter.completeWithError(e);
            }
        });

        this.emitters.add(emitter);
        emitter.onCompletion(() -> this.emitters.remove(emitter));
        emitter.onTimeout(() -> this.emitters.remove(emitter));

        return emitter;
    }


    @EventListener
    void onNotification(NotificationDto notificationDto) {
        sendSse(notificationDto);
    }

    private void sendSse(Object object) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        this.emitters.forEach(emitter -> {
            try {
                emitter.send(object, MediaType.APPLICATION_JSON);
            } catch (Exception e) {
                deadEmitters.add(emitter);
                emitter.completeWithError(e);
            }
        });

        this.emitters.remove(deadEmitters);
    }

}
