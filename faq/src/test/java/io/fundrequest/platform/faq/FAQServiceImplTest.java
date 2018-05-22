package io.fundrequest.platform.faq;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.fundrequest.platform.faq.model.FaqItem;
import io.fundrequest.platform.faq.parser.Faq;
import io.fundrequest.platform.faq.parser.Faqs;
import io.fundrequest.platform.faq.parser.Page;
import io.fundrequest.platform.github.GithubGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FAQServiceImplTest {

    private static final String OWNER = "FundRequest";
    private static final String REPO = "FAQ";
    private static final String FILE_PATH = "FAQ.xml";

    private FAQServiceImpl service;
    private GithubGateway githubGateway;
    private XmlMapper xmlMapper;
    private FaqItemMapper faqItemMapper;
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        githubGateway = mock(GithubGateway.class);
        xmlMapper = mock(XmlMapper.class);
        faqItemMapper = mock(FaqItemMapper.class);
        cacheManager = mock(CacheManager.class);
        service = new FAQServiceImpl(githubGateway, OWNER, REPO, FILE_PATH, xmlMapper, faqItemMapper, cacheManager);
    }

    @Test
    public void refreshFAQs() throws IOException {
        final String faqsXml = "fadgszdbg";

        final Map<String, List<Faq>> pages = new HashMap<>();
        final String pageName1 = "fghggfsshdg";
        final String pageName2 = "gdx";
        final String pageName3 = "khdaflk";
        final List<Faq> faq1 = new ArrayList<>();
        final List<Faq> faq2 = new ArrayList<>();
        final List<Faq> faq3 = new ArrayList<>();
        pages.put(pageName1, faq1);
        pages.put(pageName2, faq2);
        pages.put(pageName3, faq3);
        final List<FaqItem> faqItems1 = new ArrayList<>();
        final List<FaqItem> faqItems2 = new ArrayList<>();
        final List<FaqItem> faqItems3 = new ArrayList<>();
        final Cache cache = mock(Cache.class);

        when(githubGateway.getContentsAsRaw(OWNER, REPO, FILE_PATH)).thenReturn(faqsXml);
        when(xmlMapper.readValue(faqsXml, Faqs.class)).thenReturn(buildFaqsObjectWithPages(pages));
        when(faqItemMapper.mapToList(same(faq1))).thenReturn(faqItems1);
        when(faqItemMapper.mapToList(same(faq2))).thenReturn(faqItems2);
        when(faqItemMapper.mapToList(same(faq3))).thenReturn(faqItems3);
        when(cacheManager.getCache("faq")).thenReturn(cache);

        service.refreshFAQs();

        verify(cache).put(eq(pageName1), same(faqItems1));
        verify(cache).put(eq(pageName2), same(faqItems2));
        verify(cache).put(eq(pageName3), same(faqItems3));
    }

    @Test
    public void getFAQsForPage() throws IOException {
        final String faqsXml = "fadgszdbg";
        final String pageName = "fghggfsshdg";
        final List<Faq> faqs = new ArrayList<>();
        final List<FaqItem> faqItems = new ArrayList<>();

        when(githubGateway.getContentsAsRaw(OWNER, REPO, FILE_PATH)).thenReturn(faqsXml);
        when(xmlMapper.readValue(faqsXml, Faqs.class)).thenReturn(buildFaqsObjectWithPage(pageName, faqs));
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

    private Faqs buildFaqsObjectWithPage(final String pageName, final List<Faq> faqs) {
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

    private Faqs buildFaqsObjectWithPages(final Map<String, List<Faq>> pagesMap) {
        final List<Page> pages = new ArrayList<>();
        for (final String pageName : pagesMap.keySet()) {
            pages.add(Page.builder()
                          .name(pageName)
                          .faqs(pagesMap.get(pageName))
                          .build());
        }

        return Faqs.builder().pages(pages).build();
    }
}
