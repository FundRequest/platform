package io.fundrequest.core.activity;


import io.fundrequest.core.request.event.RequestCreatedEvent;
import io.fundrequest.core.user.UserDto;
import io.fundrequest.core.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.time.LocalDateTime;

import static io.fundrequest.core.request.domain.RequestSource.GITHUB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ActivityListenerTest {

    private SimpMessagingTemplate template;
    private UserService userService;
    private ActivityListener listener;

    @Before
    public void setUp() throws Exception {
        template = mock(SimpMessagingTemplate.class);
        userService = mock(UserService.class);
        listener = new ActivityListener(template, userService);
    }

    @Test
    public void onActivitySendsJmsMessage() throws Exception {
        ActivityDto activity = new ActivityDto();
        listener.onActivity(activity);

        verifyJmsMessageSentForActivity(activity);
    }

    @Test
    public void onRequestCreated() throws Exception {
        RequestCreatedEvent event = new RequestCreatedEvent("davy", "link", "title", GITHUB);
        UserDto user = new UserDto();
        when(userService.getUser(event.getCreator())).thenReturn(user);

        listener.onRequestCreated(event);

        ArgumentCaptor<ActivityDto> argumentCaptor = ArgumentCaptor.forClass(ActivityDto.class);
        verify(template).convertAndSend(eq("/topic/activities"), argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getUser()).isEqualTo(user);
        assertThat(argumentCaptor.getValue().getTitle()).isEqualTo("Request <i>" + event.getTitle() + "</i> has been created");
        assertThat(argumentCaptor.getValue().getDescription()).isEqualTo(event.getLink());
        assertThat(argumentCaptor.getValue().getDateTime()).isEqualToIgnoringMinutes(LocalDateTime.now());
    }

    private void verifyJmsMessageSentForActivity(ActivityDto activity) {
        verify(template).convertAndSend("/topic/activities", activity);
    }
}