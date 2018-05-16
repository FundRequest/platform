package io.fundrequest.platform.github.scraper;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GithubStatusResolverTest {

    private GithubStatusResolver parser;

    @Before
    public void setUp() {
        parser = new GithubStatusResolver();
    }

    @Test
    public void parseOpen() {
        final Document document = mock(Document.class, RETURNS_DEEP_STUBS);
        final String expectedStatus = "Open";

        when(document.select("#partial-discussion-header .State").text()).thenReturn(expectedStatus);

        final String returnedStatus = parser.resolve(document);

        assertThat(returnedStatus).isEqualTo(expectedStatus);
    }

    @Test
    public void parseClosed() {
        final Document document = mock(Document.class, RETURNS_DEEP_STUBS);
        final String expectedStatus = "Closed";

        when(document.select("#partial-discussion-header .State").text()).thenReturn(expectedStatus);

        final String returnedStatus = parser.resolve(document);

        assertThat(returnedStatus).isEqualTo(expectedStatus);
    }
}