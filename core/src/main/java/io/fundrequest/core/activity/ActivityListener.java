package io.fundrequest.core.activity;

import io.fundrequest.core.request.event.RequestCreatedEvent;
import io.fundrequest.core.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
public class ActivityListener {

    private SimpMessagingTemplate template;
    private UserService userService;

    public ActivityListener(SimpMessagingTemplate template, UserService userService) {
        this.template = template;
        this.userService = userService;
    }

    private static final Logger logger = LoggerFactory.getLogger(ActivityListener.class);

    public void onActivity(ActivityDto activity) {
        logger.info("sending ws event");
        template.convertAndSend("/topic/activities", activity);
    }

    @TransactionalEventListener
    public void onRequestCreated(RequestCreatedEvent requestCreatedEvent) {
        onActivity(new ActivityDto(userService.getUser(requestCreatedEvent.getCreator()),
                "Request <i>" + requestCreatedEvent.getTitle() + "</i> has been created",
                requestCreatedEvent.getLink(), LocalDateTime.now()));
    }
}
