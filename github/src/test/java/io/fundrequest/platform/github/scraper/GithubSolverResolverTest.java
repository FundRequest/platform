package io.fundrequest.platform.github.scraper;

import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.parser.GithubResult;
import io.fundrequest.platform.github.parser.GithubUser;
import io.fundrequest.platform.github.scraper.model.GithubId;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GithubSolverResolverTest {

    private GithubSolverResolver parser;
    private GithubGateway githubGateway;

    @BeforeEach
    void setUp() {
        githubGateway = mock(GithubGateway.class);
        parser = new GithubSolverResolver(githubGateway);
    }

    @Test
    void parse() {
        final String solver = "dfgh";
        final GithubUser solverUser = GithubUser.builder().login(solver).build();
        final GithubId issueGithubId = GithubId.builder().owner("tfjgk").repo("hfcjgv").number("435").build();
        final GithubId pullrequestGithubId = GithubId.builder().owner("gb").repo("awerg").number("765").build();
        final Document doc = DocumentMockBuilder.documentBuilder()
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true)
                                                                                      .isMerged(false)
                                                                                      .withAuthor("hgfcjgv")
                                                                                      .withPullrequestReference(GithubId.builder().owner("xnbf").repo("afds").number("53").build(), false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true)
                                                                                      .isMerged(true)
                                                                                      .withAuthor("gdhfh")
                                                                                      .withPullrequestReference(pullrequestGithubId, false)
                                                                                      .build())
                                                .build();

        when(githubGateway.getPullrequest("xnbf", "afds", "53")).thenReturn(GithubResult.builder()
                                                                                        .user(GithubUser.builder().login("hgfcjgv").build())
                                                                                        .body("fixes #" + issueGithubId.getNumber())
                                                                                        .build());
        when(githubGateway.getPullrequest(pullrequestGithubId.getOwner(), pullrequestGithubId.getRepo(), pullrequestGithubId.getNumber())).thenReturn(GithubResult.builder()
                                                                                                                                                                  .user(solverUser)
                                                                                                                                                                  .body("Fixes #" + issueGithubId.getNumber())
                                                                                                                                                                  .build());

        final Optional<String> result = parser.resolve(doc, issueGithubId);

        assertThat(result).contains(solver);
    }

    @Test
    void parse_pullRequestMerged_noSolverOnPage() {
        final String solver = "dfgh";
        final GithubUser solverUser = GithubUser.builder().login(solver).build();
        final GithubId issueGithubId = GithubId.builder().owner("tfjgk").repo("hfcjgv").number("435").build();
        final GithubId pullrequestGithubId = GithubId.builder().owner("gb").repo("awerg").number("765").build();
        final Document doc = DocumentMockBuilder.documentBuilder()
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true)
                                                                                      .isMerged(false)
                                                                                      .withAuthor("hgfcjgv")
                                                                                      .withPullrequestReference(GithubId.builder().owner("xnbf").repo("afds").number("53").build(), false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true)
                                                                                      .isMerged(true)
                                                                                      .withAuthor("")
                                                                                      .withPullrequestReference(pullrequestGithubId, false)
                                                                                      .build())
                                                .build();
        when(githubGateway.getPullrequest("xnbf", "afds", "53")).thenReturn(GithubResult.builder()
                                                                                        .user(GithubUser.builder().login("hgfcjgv").build())
                                                                                        .body("fixes #" + issueGithubId.getNumber())
                                                                                        .build());
        when(githubGateway.getPullrequest(pullrequestGithubId.getOwner(), pullrequestGithubId.getRepo(), pullrequestGithubId.getNumber())).thenReturn(GithubResult.builder()
                                                                                                                                                                  .user(solverUser)
                                                                                                                                                                  .body("fixes #" + issueGithubId.getNumber())
                                                                                                                                                                  .build());
        final Optional<String> result = parser.resolve(doc, issueGithubId);

        assertThat(result).contains(solver);
    }

    @Test
    void parse_noDiscussionItems() {
        final GithubId issueGithubId = GithubId.builder().owner("tfjgk").repo("hfcjgv").number("35").build();
        final Document doc = DocumentMockBuilder.documentBuilder().build();

        final Optional<String> result = parser.resolve(doc, issueGithubId);

        assertThat(result).isEmpty();
    }

    @Test
    void parse_noPullRequest() {
        final GithubId issueGithubId = GithubId.builder().owner("tfjgk").repo("hfcjgv").number("35").build();
        final Document doc = DocumentMockBuilder.documentBuilder()
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(false)
                                                                                      .build())
                                                .build();

        final Optional<String> result = parser.resolve(doc, issueGithubId);

        assertThat(result).isEmpty();
    }

    @Test
    void parse_noMergedPullRequest() {
        final GithubId issueGithubId = GithubId.builder().owner("tfjgk").repo("hfcjgv").number("35").build();
        final GithubId pullrequestGithubId = GithubId.builder().owner("gb").repo("awerg").number("765").build();
        final Document doc = DocumentMockBuilder.documentBuilder()
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true)
                                                                                      .withPullrequestReference(pullrequestGithubId, false)
                                                                                      .isMerged(false)
                                                                                      .withAuthor("hgfcjgv")
                                                                                      .build())
                                                .build();

        final Optional<String> result = parser.resolve(doc, issueGithubId);

        assertThat(result).isEmpty();
    }

    @Test
    void parse_noSolver() {
        final GithubId issueGithubId = GithubId.builder().owner("tfjgk").repo("hfcjgv").number("35").build();
        final GithubId pullrequestGithubId = GithubId.builder().owner("gb").repo("awerg").number("765").build();
        final Document doc = DocumentMockBuilder.documentBuilder()
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true)
                                                                                      .isMerged(false)
                                                                                      .withPullrequestReference(GithubId.builder().owner("xnbf").repo("afds").number("53").build(), false)
                                                                                      .withAuthor("ljhkgfdy")
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true)
                                                                                      .isMerged(true)
                                                                                      .withAuthor("")
                                                                                      .withPullrequestReference(pullrequestGithubId, true)
                                                                                      .build())
                                                .build();
        when(githubGateway.getPullrequest(pullrequestGithubId.getOwner(), pullrequestGithubId.getRepo(), pullrequestGithubId.getNumber())).thenReturn(GithubResult.builder()
                                                                                                                                                                  .user(GithubUser.builder().login("").build())
                                                                                                                                                                  .body("fixes #" + issueGithubId.getNumber())
                                                                                                                                                                  .build());

        final Optional<String> result = parser.resolve(doc, issueGithubId);

        assertThat(result).isEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"close ", "closes ", "closed ", "fix ", "fixes ", "fixed ", "resolve ", "resolves ", "resolved ", "close: ", "closes: ", "closed: ", "fix: ", "fixes: ", "fixed: ", "resolve: ",
                            "resolves: ", "resolved: ", "close:", "closes:", "closed:", "fix:", "fixes:", "fixed:", "resolve:",
                            "resolves:", "resolved:", "Close ", "Closes ", "Closed ", "Fix ", "Fixes ", "Fixed ", "Resolve ", "Resolves ", "Resolved", "close:              ", "closes: "})
    void parse_withClosingKeywords(final String keyword) {
        final String solver = "dfgh";
        final GithubUser solverUser = GithubUser.builder().login(solver).build();
        final GithubId issueGithubId = GithubId.builder().owner("tfjgk").repo("hfcjgv").number("435").build();
        final GithubId pullrequestGithubId = GithubId.builder().owner("gb").repo("awerg").number("765").build();
        final Document doc = DocumentMockBuilder.documentBuilder()
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true)
                                                                                      .isMerged(false)
                                                                                      .withAuthor("hgfcjgv")
                                                                                      .withPullrequestReference(GithubId.builder().owner("xnbf").repo("afds").number("53").build(), false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true)
                                                                                      .isMerged(true)
                                                                                      .withAuthor("gdhfh")
                                                                                      .withPullrequestReference(pullrequestGithubId, false)
                                                                                      .build())
                                                .build();

        when(githubGateway.getPullrequest("xnbf", "afds", "53")).thenReturn(GithubResult.builder()
                                                                                        .user(GithubUser.builder().login("hgfcjgv").build())
                                                                                        .body("fixes #" + issueGithubId.getNumber())
                                                                                        .build());
        when(githubGateway.getPullrequest(pullrequestGithubId.getOwner(), pullrequestGithubId.getRepo(), pullrequestGithubId.getNumber())).thenReturn(GithubResult.builder()
                                                                                                                                                                  .user(solverUser)
                                                                                                                                                                  .body(String.format("%s#%s",
                                                                                                                                                                                      keyword,
                                                                                                                                                                                      issueGithubId.getNumber()))
                                                                                                                                                                  .build());

        final Optional<String> result = parser.resolve(doc, issueGithubId);

        assertThat(result).contains(solver);
    }

    @Test
    void parse_noClosingKeyword() {
        final String solver = "dfgh";
        final GithubUser solverUser = GithubUser.builder().login(solver).build();
        final GithubId issueGithubId = GithubId.builder().owner("tfjgk").repo("hfcjgv").number("435").build();
        final GithubId pullrequestGithubId = GithubId.builder().owner("gb").repo("awerg").number("765").build();
        final Document doc = DocumentMockBuilder.documentBuilder()
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true)
                                                                                      .isMerged(false)
                                                                                      .withAuthor("hgfcjgv")
                                                                                      .withPullrequestReference(GithubId.builder().owner("xnbf").repo("afds").number("53").build(), false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true)
                                                                                      .isMerged(true)
                                                                                      .withAuthor("gdhfh")
                                                                                      .withPullrequestReference(pullrequestGithubId, false)
                                                                                      .build())
                                                .build();

        when(githubGateway.getPullrequest("xnbf", "afds", "53")).thenReturn(GithubResult.builder()
                                                                                        .user(GithubUser.builder().login("hgfcjgv").build())
                                                                                        .body("fixes #" + issueGithubId.getNumber())
                                                                                        .build());
        when(githubGateway.getPullrequest(pullrequestGithubId.getOwner(), pullrequestGithubId.getRepo(), pullrequestGithubId.getNumber())).thenReturn(GithubResult.builder()
                                                                                                                                                                  .user(solverUser)
                                                                                                                                                                  .body("")
                                                                                                                                                                  .build());

        final Optional<String> result = parser.resolve(doc, issueGithubId);

        assertThat(result).isEmpty();
    }

    @Test
    void parse_noClosingKeywordReferenceToIssue() {
        final String solver = "dfgh";
        final GithubUser solverUser = GithubUser.builder().login(solver).build();
        final GithubId issueGithubId = GithubId.builder().owner("tfjgk").repo("hfcjgv").number("435").build();
        final GithubId pullrequestGithubId = GithubId.builder().owner("gb").repo("awerg").number("765").build();
        final Document doc = DocumentMockBuilder.documentBuilder()
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true)
                                                                                      .isMerged(false)
                                                                                      .withAuthor("hgfcjgv")
                                                                                      .withPullrequestReference(GithubId.builder().owner("xnbf").repo("afds").number("53").build(), false)
                                                                                      .build())
                                                .addDiscussionItem(DocumentMockBuilder.discussionItemBuilder()
                                                                                      .isPullRequest(true)
                                                                                      .isMerged(true)
                                                                                      .withAuthor("gdhfh")
                                                                                      .withPullrequestReference(pullrequestGithubId, false)
                                                                                      .build())
                                                .build();

        when(githubGateway.getPullrequest("xnbf", "afds", "53")).thenReturn(GithubResult.builder()
                                                                                        .user(GithubUser.builder().login("hgfcjgv").build())
                                                                                        .body("fixes #" + issueGithubId.getNumber())
                                                                                        .build());
        when(githubGateway.getPullrequest(pullrequestGithubId.getOwner(), pullrequestGithubId.getRepo(), pullrequestGithubId.getNumber())).thenReturn(GithubResult.builder()
                                                                                                                                                                  .user(solverUser)
                                                                                                                                                                  .body("#" + issueGithubId.getNumber())
                                                                                                                                                                  .build());

        final Optional<String> result = parser.resolve(doc, issueGithubId);

        assertThat(result).isEmpty();
    }
}
