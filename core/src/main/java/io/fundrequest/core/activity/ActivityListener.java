package io.fundrequest.core.activity;

import io.fundrequest.core.request.event.RequestCreatedEvent;
import io.fundrequest.core.user.UserService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;

@Component
public class ActivityListener {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private UserService userService;

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

    @EventListener
    public void onLogin(AuthenticationSuccessEvent event) {
//        ((KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getAccount().getKeycloakSecurityContext().getToken();
//        onActivity(new ActivityDto(userService.getUser(event.getAuthentication().getName()),
//                "has logged in","has logged in", LocalDateTime.now()));
    }

}
