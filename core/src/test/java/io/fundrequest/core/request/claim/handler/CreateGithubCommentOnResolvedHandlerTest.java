package io.fundrequest.core.request.claim.handler;

import io.fundrequest.core.request.claim.event.RequestClaimableEvent;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.github.CreateGithubComment;
import io.fundrequest.platform.github.GithubCommentFactory;
import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.scraper.GithubScraper;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CreateGithubCommentOnResolvedHandlerTest {

    private CreateGithubCommentOnResolvedHandler handler;
    private GithubGateway githubGateway;
    private GithubCommentFactory githubCommentFactory;
    private GithubScraper githubScraper;

    @Before
    public void setUp() {
        githubGateway = mock(GithubGateway.class);
        githubCommentFactory = mock(GithubCommentFactory.class);
        githubScraper = mock(GithubScraper.class);
        handler = new CreateGithubCommentOnResolvedHandler(githubGateway, githubScraper, githubCommentFactory, true);
    }

    @Test
    public void ignoresGithubComment() {
        handler = new CreateGithubCommentOnResolvedHandler(githubGateway, githubScraper, githubCommentFactory, false);

        handler.createGithubCommentOnRequestClaimable(mock((RequestClaimableEvent.class)));

        verifyZeroInteractions(githubGateway);
    }

    @Test
    public void postsGithubComment() {
        final RequestClaimableEvent event = new RequestClaimableEvent(RequestDtoMother.freeCodeCampNoUserStories(), LocalDateTime.now());
        final String expectedMessage = "ytufg";
        final RequestDto request = event.getRequestDto();
        final IssueInformationDto issueInformation = request.getIssueInformation();
        final String solver = "gdhfjghiuyutfyd";
        final ArgumentCaptor<CreateGithubComment> createGithubCommentArgumentCaptor = ArgumentCaptor.forClass(CreateGithubComment.class);

        when(githubScraper.fetchGithubIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(GithubIssue.builder()
                                                                                                                                                          .solver(solver)
                                                                                                                                                          .build());
        when(githubCommentFactory.createResolvedComment(request.getId(), solver)).thenReturn(expectedMessage);

        handler.createGithubCommentOnRequestClaimable(event);

        verify(githubGateway).createCommentOnIssue(eq(issueInformation.getOwner()),
                                                   eq(issueInformation.getRepo()),
                                                   eq(issueInformation.getNumber()),
                                                   createGithubCommentArgumentCaptor.capture());
        assertThat(createGithubCommentArgumentCaptor.getValue().getBody()).isEqualTo(expectedMessage);
    }

    @Test
    public void postsGithubComment_noSolverFound() {
        final RequestClaimableEvent event = new RequestClaimableEvent(RequestDtoMother.freeCodeCampNoUserStories(), LocalDateTime.now());
        final RequestDto request = event.getRequestDto();
        final IssueInformationDto issueInformation = request.getIssueInformation();

        when(githubScraper.fetchGithubIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(GithubIssue.builder().build());

        try {
            handler.createGithubCommentOnRequestClaimable(event);
            fail("RuntimeException expected");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("No solver found for request " + request.getId());
        }
    }
}
