package io.fundrequest.core.request.claim.handler;

import io.fundrequest.core.request.claim.event.RequestClaimableEvent;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.github.CreateGithubComment;
import io.fundrequest.platform.github.GitHubCommentFactory;
import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.GithubSolverResolver;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;

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
    private GitHubCommentFactory gitHubCommentFactory;
    private GithubSolverResolver githubSolverResolver;

    @Before
    public void setUp() {
        githubGateway = mock(GithubGateway.class);
        gitHubCommentFactory = mock(GitHubCommentFactory.class);
        githubSolverResolver = mock(GithubSolverResolver.class);
        handler = new CreateGithubCommentOnResolvedHandler(githubGateway, githubSolverResolver, gitHubCommentFactory, true);
    }

    @Test
    public void ignoresGithubComment() {
        handler = new CreateGithubCommentOnResolvedHandler(githubGateway, githubSolverResolver, gitHubCommentFactory, false);

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

        when(githubSolverResolver.solveResolver(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(Optional.of(solver));
        when(gitHubCommentFactory.createResolvedComment(request.getId(), solver)).thenReturn(expectedMessage);

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
        final String expectedMessage = "ytufg";
        final RequestDto request = event.getRequestDto();
        final IssueInformationDto issueInformation = request.getIssueInformation();
        final String solver = "gdhfjghiuyutfyd";
        final ArgumentCaptor<CreateGithubComment> createGithubCommentArgumentCaptor = ArgumentCaptor.forClass(CreateGithubComment.class);

        when(githubSolverResolver.solveResolver(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(Optional.empty());
        when(gitHubCommentFactory.createResolvedComment(request.getId(), solver)).thenReturn(expectedMessage);

        try {
            handler.createGithubCommentOnRequestClaimable(event);
            fail("RuntimeException expected");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("No solver found for request " + request.getId());
        }
    }
}
