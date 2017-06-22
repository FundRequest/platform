package io.fundrequest.core.activity;

import io.fundrequest.core.request.event.RequestCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
public class ActivityListener {

    @Autowired
    private SimpMessagingTemplate template;

    private static final Logger logger = LoggerFactory.getLogger(ActivityListener.class);

    public void onActivity(Activity activity) {
        logger.info("sending ws event");
        template.convertAndSend("/topic/activities", activity);
    }

    @TransactionalEventListener
    public void onRequestCreated(RequestCreatedEvent requestCreatedEvent) {
        onActivity(new Activity(requestCreatedEvent.getCreator(),
                "Request <i>" + requestCreatedEvent.getTitle() + "</i> has been created",
                requestCreatedEvent.getLink(), LocalDateTime.now()));
    }

}
