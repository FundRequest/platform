package io.fundrequest.platform.faq;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.fundrequest.platform.faq.model.FaqItem;
import io.fundrequest.platform.faq.parser.Faq;
import io.fundrequest.platform.faq.parser.Faqs;
import io.fundrequest.platform.faq.parser.Page;
import io.fundrequest.platform.github.GithubGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FAQServiceTest {

    private static final String OWNER = "FundRequest";
    private static final String REPO = "FAQ";
    private static final String FILE_PATH = "FAQ.xml";

    private FAQService service;
    private GithubGateway githubGateway;
    private XmlMapper xmlMapper;
    private FaqItemMapper faqItemMapper;

    @BeforeEach
    void setUp() {
        githubGateway = mock(GithubGateway.class);
        xmlMapper = mock(XmlMapper.class);
        faqItemMapper = mock(FaqItemMapper.class);

        service = new FAQService(githubGateway, OWNER, REPO, FILE_PATH, xmlMapper, faqItemMapper);
    }

    @Test
    public void getFAQsForPage() throws IOException {
        final String faqsXml = "fadgszdbg";
        final String pageName = "fghggfsshdg";
        final List<Faq> faqs = new ArrayList<>();
        final List<FaqItem> faqItems = new ArrayList<>();

        when(githubGateway.getContentsAsRaw(OWNER, REPO, FILE_PATH)).thenReturn(faqsXml);
        when(xmlMapper.readValue(faqsXml, Faqs.class)).thenReturn(buildFaqsObject(pageName, faqs));
        when(faqItemMapper.mapToList(same(faqs))).thenReturn(faqItems);

        final List<FaqItem> result = service.getFAQsForPage(pageName);

        assertThat(result).isSameAs(faqItems);
    }

    @Test
    public void getFAQsForPage_xmlMapperThrowsException() throws IOException {
        final String faqsXml = "vgjk";
        final String pageName = "nbn";
        final IOException ioException = new IOException();

        when(githubGateway.getContentsAsRaw(OWNER, REPO, FILE_PATH)).thenReturn(faqsXml);
        doThrow(ioException).when(xmlMapper).readValue(faqsXml, Faqs.class);

        try {
            service.getFAQsForPage(pageName);
            fail("A RuntimeException(\"Something went wrong while trying to parse FAQ.xml\", e) was expected to be thrown");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Something went wrong while trying to parse FAQ.xml");
            assertThat(e.getCause()).isEqualTo(ioException);
        }
    }

    private Faqs buildFaqsObject(final String pageName, final List<Faq> faqs) {
        return Faqs.builder()
                   .pages(Arrays.asList(Page.builder()
                                            .name("afdsgd")
                                            .faqs(Arrays.asList(Faq.builder()
                                                                   .title("afdsgd - FAQ 1")
                                                                   .filePath("afdsgd/FAQ1.md")
                                                                   .build(),
                                                                Faq.builder()
                                                                   .title("afdsgd - FAQ 2")
                                                                   .filePath("afdsgd/FAQ2.md")
                                                                   .build(),
                                                                Faq.builder()
                                                                   .title("afdsgd - FAQ 3")
                                                                   .filePath("afdsgd/FAQ3.md")
                                                                   .build()))
                                            .build(),
                                        Page.builder()
                                            .name(pageName)
                                            .faqs(faqs)
                                            .build()))
                   .build();
    }
}
