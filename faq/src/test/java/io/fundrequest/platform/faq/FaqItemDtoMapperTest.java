package io.fundrequest.platform.faq;

import io.fundrequest.common.infrastructure.JsoupSpringWrapper;
import io.fundrequest.platform.faq.model.FaqItemDto;
import io.fundrequest.platform.faq.parser.Faq;
import io.fundrequest.platform.github.GithubGateway;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FaqItemDtoMapperTest {

    private static final String OWNER = "hjgfkh";
    private static final String REPO = "asd";
    private static final String BRANCH = "develop";

    private FaqItemDtoMapper mapper;
    private GithubGateway githubGateway;
    private JsoupSpringWrapper jsoup;

    @BeforeEach
    public void setUp() {
        githubGateway = mock(GithubGateway.class);
        jsoup = mock(JsoupSpringWrapper.class);
        mapper = new FaqItemDtoMapper(githubGateway, OWNER, REPO, BRANCH, jsoup);
    }

    @Test
    public void map() {
        final String title = "ghfgjhk";
        final String filePath = "iouyi";
        final String contentHtml = "dsafg";
        final String expectedBody = "<h1>Cdfghjhkj</h1>";
        final Document contentDocument = mock(Document.class);
        final Elements markdownElements = mock(Elements.class);
        final Element markdownElement = mock(Element.class);

        when(githubGateway.getContentsAsHtml(OWNER, REPO, BRANCH, filePath)).thenReturn(contentHtml);
        when(jsoup.parse(contentHtml)).thenReturn(contentDocument);
        when(contentDocument.select(".markdown-body")).thenReturn(markdownElements);
        when(markdownElements.isEmpty()).thenReturn(false);
        when(markdownElements.get(0)).thenReturn(markdownElement);
        when(markdownElement.html()).thenReturn(expectedBody);

        final FaqItemDto result = mapper.map(Faq.builder().title(title).filePath(filePath).build());

        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getBody()).isEqualTo(expectedBody);
    }

    @Test
    public void map_emptyMD() {
        final String title = "ghfgjhk";
        final String filePath = "iouyi";
        final String contentHtml = "dsafg";
        final String expectedBody = "<h1>Cdfghjhkj</h1>";
        final Document contentDocument = mock(Document.class);
        final Elements markdownElements = mock(Elements.class);
        final Element markdownElement = mock(Element.class);

        when(githubGateway.getContentsAsHtml(OWNER, REPO, BRANCH, filePath)).thenReturn(contentHtml);
        when(jsoup.parse(contentHtml)).thenReturn(contentDocument);
        when(contentDocument.select(".markdown-body")).thenReturn(markdownElements);
        when(markdownElements.isEmpty()).thenReturn(true);
        doThrow(new IndexOutOfBoundsException()).when(markdownElements).get(0);

        final FaqItemDto result = mapper.map(Faq.builder().title(title).filePath(filePath).build());

        assertThat(result).isNull();
    }

    @Test
    public void map_exception() {
        final String title = "ghfgjhk";
        final String filePath = "iouyi";
        final String contentHtml = "dsafg";
        final String expectedBody = "<h1>Cdfghjhkj</h1>";
        final Document contentDocument = mock(Document.class);
        final Elements markdownElements = mock(Elements.class);
        final Element markdownElement = mock(Element.class);

        when(githubGateway.getContentsAsHtml(OWNER, REPO, BRANCH, filePath)).thenReturn(contentHtml);
        when(jsoup.parse(contentHtml)).thenReturn(contentDocument);
        when(contentDocument.select(".markdown-body")).thenReturn(markdownElements);

        try {
            final FaqItemDto result = mapper.map(Faq.builder().title(title).filePath(filePath).build());
            fail("A new RuntimException(\"Something went wrong during the mapping of FaqItem '" + title + "'\")");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Something went wrong during the mapping of FaqItem '" + title + "'");
            assertThat(e).hasCauseInstanceOf(NullPointerException.class);
        }
    }

    @Test
    public void mapNull() {
        assertThat(mapper.map(null)).isNull();
    }
}
