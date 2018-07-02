package io.fundrequest.platform.github.scraper;


import io.fundrequest.common.infrastructure.JsoupSpringWrapper;
import io.fundrequest.platform.github.scraper.model.GithubId;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GithubScraperTest {

    private GithubScraper scraper;
    private JsoupSpringWrapper jsoup;
    private GithubSolverResolver solverParser;
    private GithubStatusResolver statusParser;

    @Before
    public void setUp() {
        jsoup = mock(JsoupSpringWrapper.class, RETURNS_DEEP_STUBS);
        solverParser = mock(GithubSolverResolver.class);
        statusParser = mock(GithubStatusResolver.class);
        scraper = new GithubScraper(jsoup, solverParser, statusParser);
    }

    @Test
    public void fetchGithubIssue() throws IOException {
        final String owner = "fdv";
        final String repo = "sdfgdh";
        final String number = "46576";
        final String expectedSolver = "gfhcgj";
        final String expectedStatus = "Open";
        final Document document = mock(Document.class);

        when(jsoup.connect("https://github.com/" + owner + "/" + repo + "/issues/" + number).get()).thenReturn(document);
        when(solverParser.resolve(document, GithubId.builder().owner(owner).repo(repo).number(number).build())).thenReturn(Optional.of(expectedSolver));
        when(statusParser.resolve(document)).thenReturn(expectedStatus);

        final GithubIssue returnedIssue = scraper.fetchGithubIssue(owner, repo, number);

        assertThat(returnedIssue.getNumber()).isEqualTo(number);
        assertThat(returnedIssue.getSolver()).isEqualTo(expectedSolver);
        assertThat(returnedIssue.getStatus()).isEqualTo(expectedStatus);
    }
}
