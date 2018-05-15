package io.fundrequest.platform.github.scraper;


import io.fundrequest.common.infrastructure.JsoupSpringWrapper;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GithubScraperTest {

    private GithubScraper scraper;
    private JsoupSpringWrapper jsoup;
    private GithubSolverParser solverParser;
    private GithubStatusParser statusParser;

    @Before
    public void setUp() {
        jsoup = mock(JsoupSpringWrapper.class, RETURNS_DEEP_STUBS);
        solverParser = mock(GithubSolverParser.class);
        statusParser = mock(GithubStatusParser.class);
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
        when(solverParser.parse(document, owner, repo)).thenReturn(expectedSolver);
        when(statusParser.parse(document)).thenReturn(expectedStatus);

        final GithubIssue returnedIssue = scraper.fetchGithubIssue(owner, repo, number);

        assertThat(returnedIssue.getNumber()).isEqualTo(number);
        assertThat(returnedIssue.getSolver()).isEqualTo(expectedSolver);
        assertThat(returnedIssue.getStatus()).isEqualTo(expectedStatus);
    }
}
