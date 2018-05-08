package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.view.FundDtoMother;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.github.CreateGithubComment;
import io.fundrequest.platform.github.GitHubCommentFactory;
import io.fundrequest.platform.github.GithubGateway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CreateGithubCommentOnFundHandlerTest {

    private CreateGithubCommentOnFundHandler handler;
    private GithubGateway githubGateway;
    private GitHubCommentFactory gitHubCommentFactory;

    @Before
    public void setUp() throws Exception {
        githubGateway = mock(GithubGateway.class);
        gitHubCommentFactory = mock(GitHubCommentFactory.class);
        handler = new CreateGithubCommentOnFundHandler(githubGateway, gitHubCommentFactory, true, "fundrequest-notifier");
    }

    @Test
    public void ignoresGithubComment() throws Exception {
        handler = new CreateGithubCommentOnFundHandler(githubGateway, gitHubCommentFactory, false, "fundrequest-notifier");

        handler.createGithubCommentOnRequestFunded(createEvent());

        verifyZeroInteractions(githubGateway);
    }

    @Test
    public void postsGithubComment() {
        final RequestFundedEvent event = createEvent();
        final String expectedMessage = "sgdhfjgkh";
        final IssueInformationDto issueInformation = event.getRequestDto().getIssueInformation();
        final ArgumentCaptor<CreateGithubComment> createGithubCommentArgumentCaptor = ArgumentCaptor.forClass(CreateGithubComment.class);

        when(gitHubCommentFactory.createFundedComment(event.getRequestDto().getId())).thenReturn(expectedMessage);

        handler.createGithubCommentOnRequestFunded(event);

        verify(githubGateway).createCommentOnIssue(eq(issueInformation.getOwner()), eq(issueInformation.getRepo()), eq(issueInformation.getNumber()), createGithubCommentArgumentCaptor.capture());
        assertThat(createGithubCommentArgumentCaptor.getValue().getBody()).isEqualTo(expectedMessage);
    }

    private RequestFundedEvent createEvent() {
        return new RequestFundedEvent("0xd009f45e5d999ba5c3c5ffc2551a9749919d6c8aa915106c2c21e76ab552c200", FundDtoMother.aFundDto(), RequestDtoMother.freeCodeCampNoUserStories(), LocalDateTime.now());
    }
}