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

import java.time.LocalDateTime;

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
        handler = new CreateGithubCommentOnFundHandler(githubClient, true, "fundrequest-notifier");
    }

    @Test
    public void ignoresGithubComment() throws Exception {
        handler = new CreateGithubCommentOnFundHandler(githubClient, false, "fundrequest-notifier");

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
        assertThat(createGithubCommentArgumentCaptor.getValue().getBody()).isEqualTo("Great, this issue is now funded on FundRequest: https://alpha.fundrequest.io!  \n" +
                "\n" +
                "50.33 FND was funded on 2017-12-27");

    }

    private RequestFundedEvent createEvent() {
        return new RequestFundedEvent("0xd009f45e5d999ba5c3c5ffc2551a9749919d6c8aa915106c2c21e76ab552c200", FundDtoMother.aFundDto(), RequestDtoMother.freeCodeCampNoUserStories(), LocalDateTime.now());
    }
}