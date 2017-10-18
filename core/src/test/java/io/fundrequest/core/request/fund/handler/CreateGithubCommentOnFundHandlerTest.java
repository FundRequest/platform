package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.infrastructure.github.CreateGithubComment;
import io.fundrequest.core.request.infrastructure.github.GithubClient;
import io.fundrequest.core.user.UserDtoMother;
import io.fundrequest.core.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CreateGithubCommentOnFundHandlerTest {

    private CreateGithubCommentOnFundHandler handler;
    private GithubClient githubClient;
    private UserService userService;

    @Before
    public void setUp() throws Exception {
        githubClient = mock(GithubClient.class);
        userService = mock(UserService.class);
        handler = new CreateGithubCommentOnFundHandler(githubClient, userService, true);
    }

    @Test
    public void ignoresGithubComment() throws Exception {
        handler = new CreateGithubCommentOnFundHandler(githubClient, mock(UserService.class), false);

        handler.createGithubCommentOnRequestFunded(createEvent());

        verifyZeroInteractions(githubClient);
    }

    @Test
    public void postsGithubComment() throws Exception {
        RequestFundedEvent event = createEvent();
        when(userService.getUser(event.getFunder())).thenReturn(UserDtoMother.davy());

        handler.createGithubCommentOnRequestFunded(event);

        ArgumentCaptor<CreateGithubComment> createGithubCommentArgumentCaptor = ArgumentCaptor.forClass(CreateGithubComment.class);
        verify(githubClient).createCommentOnIssue(eq(event.getOwner()), eq(event.getRepo()), eq(event.getNumber()), createGithubCommentArgumentCaptor.capture());
        assertThat(createGithubCommentArgumentCaptor.getValue().getBody()).isEqualTo("Great! davy@fundrequest.io funded 50.33 FND to this issue. For more information, go to https://fundrequest.io.");

    }

    private RequestFundedEvent createEvent() {
        return new RequestFundedEvent(1L, "davy", "repo", "10", "davy", new BigDecimal("50330000000000000000"));
    }
}