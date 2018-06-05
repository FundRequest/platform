package io.fundrequest.platform.github.scraper;

import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.parser.GithubResult;
import io.fundrequest.platform.github.parser.GithubUser;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GithubSolverResolverTest {

    private GithubSolverResolver parser;
    private GithubGateway githubGateway;

    @Before
    public void setUp() {
        githubGateway = mock(GithubGateway.class);
        parser = new GithubSolverResolver(githubGateway);
    }

    @Test
    public void parse() {
        final String owner = "tfjgk";
        final String repo = "hfcjgv";
        final String solver = "dfgh";
        final String pullrequestNumber = "765";
        final Document doc = DocumentMockBuilder.documentBuilder()
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true, "53")
                                                                                      .isMerged(false)
                                                                                      .withAuthor("hgfcjgv")
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true, pullrequestNumber)
                                                                                      .isMerged(true)
                                                                                      .withAuthor(solver)
                                                                                      .build())
                                                .build();

        when(githubGateway.getPullrequest(owner, repo, pullrequestNumber)).thenReturn(GithubResult.builder()
                                                                                                  .user(GithubUser.builder().login(solver).build())
                                                                                                  .build());

        final String returnedSolver = parser.resolve(doc, owner, repo);

        assertThat(returnedSolver).isEqualTo(solver);
    }

    @Test
    public void parse_pullRequestMerged_noSolverOnPage() {
        final String owner = "tfjgk";
        final String repo = "hfcjgv";
        final String solver = "dfgh";
        final String pullrequestNumber = "765";
        final Document doc = DocumentMockBuilder.documentBuilder()
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true, "53")
                                                                                      .isMerged(false)
                                                                                      .withAuthor("hgfcjgv")
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true, pullrequestNumber)
                                                                                      .isMerged(true)
                                                                                      .withAuthor("")
                                                                                      .build())
                                                .build();
        when(githubGateway.getPullrequest(owner, repo, pullrequestNumber)).thenReturn(GithubResult.builder()
                                                                                                  .user(GithubUser.builder().login(solver).build())
                                                                                                  .build());

        final String returnedSolver = parser.resolve(doc, owner, repo);

        assertThat(returnedSolver).isEqualTo(solver);
    }

    @Test
    public void parse_noDiscussionItems() {
        final String owner = "tfjgk";
        final String repo = "hfcjgv";
        final Document doc = DocumentMockBuilder.documentBuilder().build();

        final String returnedSolver = parser.resolve(doc, owner, repo);

        assertThat(returnedSolver).isNull();
    }

    @Test
    public void parse_noPullRequest() {
        final String owner = "tfjgk";
        final String repo = "hfcjgv";
        final Document doc = DocumentMockBuilder.documentBuilder()
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(false)
                                                                                      .build())
                                                .build();

        final String returnedSolver = parser.resolve(doc, owner, repo);

        assertThat(returnedSolver).isNull();;
    }

    @Test
    public void parse_noMergedPullRequest() {
        final String owner = "tfjgk";
        final String repo = "hfcjgv";
        final Document doc = DocumentMockBuilder.documentBuilder()
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true, "53")
                                                                                      .isMerged(false)
                                                                                      .withAuthor("hgfcjgv")
                                                                                      .build())
                                                .build();

        final String returnedSolver = parser.resolve(doc, owner, repo);

        assertThat(returnedSolver).isNull();
    }

    @Test
    public void parse_noSolver() {
        final String owner = "tfjgk";
        final String repo = "hfcjgv";
        final String pullrequestNumber = "43";
        final Document doc = DocumentMockBuilder.documentBuilder()
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true, "31")
                                                                                      .isMerged(false)
                                                                                      .withAuthor("ljhkgfdy")
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true, pullrequestNumber)
                                                                                      .isMerged(true)
                                                                                      .withAuthor("")
                                                                                      .build())
                                                .build();
        when(githubGateway.getPullrequest(owner, repo, pullrequestNumber)).thenReturn(GithubResult.builder()
                                                                                                  .user(GithubUser.builder().login("").build())
                                                                                                  .build());

        final String returnedSolver = parser.resolve(doc, owner, repo);

        assertThat(returnedSolver).isNull();
    }
}
