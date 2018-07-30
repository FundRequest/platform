package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.github.CreateGithubComment;
import io.fundrequest.platform.github.GithubCommentFactory;
import io.fundrequest.platform.github.GithubGateway;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CreateGithubCommentOnFundHandlerTest {

    private CreateGithubCommentOnFundHandler handler;
    private GithubGateway githubGateway;
    private GithubCommentFactory githubCommentFactory;
    private RequestService requestService;

    @Before
    public void setUp() {
        githubGateway = mock(GithubGateway.class);
        githubCommentFactory = mock(GithubCommentFactory.class);
        requestService = mock(RequestService.class);
        handler = new CreateGithubCommentOnFundHandler(githubGateway, githubCommentFactory, requestService, true, "fundrequest-notifier");
    }

    @Test
    public void ignoresGithubComment() {
        handler = new CreateGithubCommentOnFundHandler(githubGateway, githubCommentFactory, requestService, false, "fundrequest-notifier");

        handler.createGithubCommentOnRequestFunded(createEvent(65478L));

        verifyZeroInteractions(githubGateway);
    }

    @Test
    public void postsGithubComment() {
        final long requestId = 65478L;
        final RequestFundedEvent event = createEvent(requestId);
        final String expectedMessage = "sgdhfjgkh";
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        final IssueInformationDto issueInformation = requestDto.getIssueInformation();
        final ArgumentCaptor<CreateGithubComment> createGithubCommentArgumentCaptor = ArgumentCaptor.forClass(CreateGithubComment.class);

        when(githubCommentFactory.createFundedComment(requestId, issueInformation.getNumber())).thenReturn(expectedMessage);
        when(requestService.findRequest(requestId)).thenReturn(requestDto);

        handler.createGithubCommentOnRequestFunded(event);

        verify(githubGateway).createCommentOnIssue(eq(issueInformation.getOwner()), eq(issueInformation.getRepo()), eq(issueInformation.getNumber()), createGithubCommentArgumentCaptor.capture());

        final CreateGithubComment githubComment = createGithubCommentArgumentCaptor.getValue();
        assertThat(githubComment.getBody()).isEqualTo(expectedMessage);
    }

    private RequestFundedEvent createEvent(long requestId) {
        return RequestFundedEvent.builder()
                                 .requestId(requestId)
                                 .build();
    }
}