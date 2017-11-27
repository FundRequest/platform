package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.infrastructure.github.CreateGithubComment;
import io.fundrequest.core.request.infrastructure.github.GithubClient;
import io.fundrequest.core.request.view.FundDtoMother;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class CreateGithubCommentOnFundHandlerTest {

    private CreateGithubCommentOnFundHandler handler;
    private GithubClient githubClient;

    @Before
    public void setUp() throws Exception {
        githubClient = mock(GithubClient.class);
        handler = new CreateGithubCommentOnFundHandler(githubClient, true);
    }

    @Test
    public void ignoresGithubComment() throws Exception {
        handler = new CreateGithubCommentOnFundHandler(githubClient, false);

        handler.createGithubCommentOnRequestFunded(createEvent());

        verifyZeroInteractions(githubClient);
    }

    @Test
    public void postsGithubComment() throws Exception {
        RequestFundedEvent event = createEvent();

        handler.createGithubCommentOnRequestFunded(event);

        ArgumentCaptor<CreateGithubComment> createGithubCommentArgumentCaptor = ArgumentCaptor.forClass(CreateGithubComment.class);
        IssueInformationDto issueInformation = event.getRequestDto().getIssueInformation();
        verify(githubClient).createCommentOnIssue(eq(issueInformation.getOwner()), eq(issueInformation.getRepo()), eq(issueInformation.getNumber()), createGithubCommentArgumentCaptor.capture());
        assertThat(createGithubCommentArgumentCaptor.getValue().getBody()).isEqualTo("Great! 50.33 FND was added to this issue. For more information, go to https://alpha.fundrequest.io.");

    }

    private RequestFundedEvent createEvent() {
        return new RequestFundedEvent(FundDtoMother.aFundDto(), RequestDtoMother.freeCodeCampNoUserStories());
    }
}