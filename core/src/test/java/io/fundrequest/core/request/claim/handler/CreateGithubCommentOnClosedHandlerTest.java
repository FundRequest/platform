package io.fundrequest.core.request.claim.handler;

import io.fundrequest.core.request.claim.dto.ClaimDto;
import io.fundrequest.core.request.claim.event.RequestClaimedEvent;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.github.CreateGithubComment;
import io.fundrequest.platform.github.GithubCommentFactory;
import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.parser.GithubIssueCommentsResult;
import io.fundrequest.platform.github.parser.GithubUser;
import io.fundrequest.platform.github.scraper.GithubScraper;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class CreateGithubCommentOnClosedHandlerTest {

    private static final String SOLVER = "gdhfjghiuyutfyd";
    private static final String EXPECTED_MESSAGE = "ytufg";

    private CreateGithubCommentOnClosedHandler handler;

    private GithubGateway githubGateway;
    private GithubCommentFactory githubCommentFactory;
    private GithubScraper githubScraper;
    private String githubUser = "ytruyt";

    @Before
    public void setUp() {
        githubGateway = mock(GithubGateway.class);
        githubCommentFactory = mock(GithubCommentFactory.class);
        githubScraper = mock(GithubScraper.class);
        handler = new CreateGithubCommentOnClosedHandler(githubGateway, githubScraper, githubCommentFactory, true, githubUser);
    }

    @Test
    public void ignoresGithubComment() {
        handler = new CreateGithubCommentOnClosedHandler(githubGateway, githubScraper, githubCommentFactory, false, githubUser);

        handler.createGithubCommentOnRequestClaimed(mock((RequestClaimedEvent.class)));

        verifyZeroInteractions(githubGateway);
    }

    @Test
    public void postsGithubComment_lessThan2CommentsPresent() {
        final RequestClaimedEvent event = new RequestClaimedEvent("1324354", RequestDtoMother.freeCodeCampNoUserStories(), mock(ClaimDto.class), SOLVER, LocalDateTime.now());
        final RequestDto request = event.getRequestDto();
        final IssueInformationDto issueInformation = request.getIssueInformation();
        final ArgumentCaptor<CreateGithubComment> createGithubCommentArgumentCaptor = ArgumentCaptor.forClass(CreateGithubComment.class);
        final List<GithubIssueCommentsResult> existingComments = Arrays.asList(createCommentFromGithubUserMock(0));

        when(githubGateway.getCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(existingComments);
        when(githubScraper.fetchGithubIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(GithubIssue.builder().solver(SOLVER).build());
        when(githubCommentFactory.createClosedComment(request.getId(), SOLVER)).thenReturn(EXPECTED_MESSAGE);

        handler.createGithubCommentOnRequestClaimed(event);

        final InOrder inOrder = inOrder(githubGateway);
        inOrder.verify(githubGateway).evictCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber());
        inOrder.verify(githubGateway).getCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber());
        inOrder.verify(githubGateway).createCommentOnIssue(eq(issueInformation.getOwner()),
                                                   eq(issueInformation.getRepo()),
                                                   eq(issueInformation.getNumber()),
                                                   createGithubCommentArgumentCaptor.capture());
        assertThat(createGithubCommentArgumentCaptor.getValue().getBody()).isEqualTo(EXPECTED_MESSAGE);
    }

    @Test
    public void postsGithubComment_2CommentsPresent() {
        final RequestClaimedEvent event = new RequestClaimedEvent("1324354", RequestDtoMother.freeCodeCampNoUserStories(), mock(ClaimDto.class), SOLVER, LocalDateTime.now());
        final RequestDto request = event.getRequestDto();
        final IssueInformationDto issueInformation = request.getIssueInformation();
        final ArgumentCaptor<CreateGithubComment> createGithubCommentArgumentCaptor = ArgumentCaptor.forClass(CreateGithubComment.class);
        final GithubIssueCommentsResult firstComment = createCommentFromGithubUserMock(-2);
        final GithubIssueCommentsResult secondComment = createCommentFromGithubUserMock(-1);
        final List<GithubIssueCommentsResult> existingComments = Arrays.asList(firstComment, secondComment);

        when(githubGateway.getCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(existingComments);
        when(githubScraper.fetchGithubIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(GithubIssue.builder().solver(SOLVER).build());
        when(githubCommentFactory.createClosedComment(request.getId(), SOLVER)).thenReturn(EXPECTED_MESSAGE);

        handler.createGithubCommentOnRequestClaimed(event);

        final InOrder inOrder = inOrder(githubGateway);
        inOrder.verify(githubGateway).evictCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber());
        inOrder.verify(githubGateway).getCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber());
        inOrder.verify(githubGateway).editCommentOnIssue(eq(issueInformation.getOwner()),
                                                 eq(issueInformation.getRepo()),
                                                 eq(secondComment.getId()),
                                                 createGithubCommentArgumentCaptor.capture());
        assertThat(createGithubCommentArgumentCaptor.getValue().getBody()).isEqualTo(EXPECTED_MESSAGE);
    }

    @Test
    public void postsGithubComment_moreThan2CommentsPresent() {
        final RequestClaimedEvent event = new RequestClaimedEvent("1324354", RequestDtoMother.freeCodeCampNoUserStories(), mock(ClaimDto.class), SOLVER, LocalDateTime.now());
        final RequestDto request = event.getRequestDto();
        final IssueInformationDto issueInformation = request.getIssueInformation();
        final ArgumentCaptor<CreateGithubComment> createGithubCommentArgumentCaptor = ArgumentCaptor.forClass(CreateGithubComment.class);
        final GithubIssueCommentsResult firstComment = createCommentFromGithubUserMock(-3);
        final GithubIssueCommentsResult secondComment = createCommentFromGithubUserMock(-1);
        final GithubIssueCommentsResult thirdComment = createCommentFromGithubUserMock(-2);
        final List<GithubIssueCommentsResult> existingComments = Arrays.asList(firstComment, secondComment, thirdComment);

        when(githubGateway.getCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(existingComments);
        when(githubScraper.fetchGithubIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(GithubIssue.builder().solver(SOLVER).build());
        when(githubCommentFactory.createClosedComment(request.getId(), SOLVER)).thenReturn(EXPECTED_MESSAGE);

        handler.createGithubCommentOnRequestClaimed(event);

        final InOrder inOrder = inOrder(githubGateway);
        inOrder.verify(githubGateway).evictCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber());
        inOrder.verify(githubGateway).getCommentsForIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber());
        inOrder.verify(githubGateway).editCommentOnIssue(eq(issueInformation.getOwner()),
                                                 eq(issueInformation.getRepo()),
                                                 eq(secondComment.getId()),
                                                 createGithubCommentArgumentCaptor.capture());
        assertThat(createGithubCommentArgumentCaptor.getValue().getBody()).isEqualTo(EXPECTED_MESSAGE);
    }

    @Test
    public void postsGithubComment_noSolverFound() {
        final RequestClaimedEvent event = new RequestClaimedEvent("1324354", RequestDtoMother.freeCodeCampNoUserStories(), mock(ClaimDto.class), SOLVER, LocalDateTime.now());
        final RequestDto request = event.getRequestDto();
        final IssueInformationDto issueInformation = request.getIssueInformation();

        when(githubScraper.fetchGithubIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(GithubIssue.builder().build());

        try {
            handler.createGithubCommentOnRequestClaimed(event);
            fail("RuntimeException expected");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("No solver found for request " + request.getId());
        }
    }

    private GithubIssueCommentsResult createCommentFromGithubUserMock(final long createdOfsetDays) {
        return new GithubIssueCommentsResult(Math.abs(UUID.randomUUID().getMostSignificantBits()),
                                             GithubUser.builder().login(githubUser).build(),
                                             "hdfgjh",
                                             OffsetDateTime.now().plusDays(createdOfsetDays),
                                             OffsetDateTime.now().plusDays(createdOfsetDays),
                                             "gdfhgjhhjlkj;lk");
    }
}
