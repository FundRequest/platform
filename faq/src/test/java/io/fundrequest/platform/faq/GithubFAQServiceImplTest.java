package io.fundrequest.platform.faq;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.fundrequest.platform.faq.model.FaqItemDto;
import io.fundrequest.platform.faq.model.FaqItemsDto;
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
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.refEq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GithubFAQServiceImplTest {

    private static final String OWNER = "FundRequest";
    private static final String REPO = "FAQ";
    private static final String MASTER = "master";
    private static final String FILE_PATH = "FAQ.xml";

    private GithubFAQServiceImpl service;
    private GithubGateway githubGateway;
    private XmlMapper xmlMapper;
    private FaqItemDtoMapper faqItemDtoMapper;
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        githubGateway = mock(GithubGateway.class);
        xmlMapper = mock(XmlMapper.class);
        faqItemDtoMapper = mock(FaqItemDtoMapper.class);
        cacheManager = mock(CacheManager.class);
        service = new GithubFAQServiceImpl(githubGateway, OWNER, REPO, MASTER, FILE_PATH, xmlMapper, faqItemDtoMapper, cacheManager);
    }

    @Test
    public void refreshFAQs() throws IOException {
        final String faqsXml = "fadgszdbg";
        final List<FaqItemDto> faqItems1 = Arrays.asList(mock(FaqItemDto.class));
        final List<FaqItemDto> faqItems2 = Arrays.asList(mock(FaqItemDto.class), mock(FaqItemDto.class));
        final List<FaqItemDto> faqItems3 = Arrays.asList(mock(FaqItemDto.class), null, mock(FaqItemDto.class));
        final Cache cache = mock(Cache.class);
        final Page page1 = Page.builder().name("gfs").subtitle("oenoewfxsc").faqs(new ArrayList<>()).build();
        final Page page2 = Page.builder().name("ghc").subtitle("hfgdljhhkj").faqs(new ArrayList<>()).build();
        final Page page3 = Page.builder().name("daf").subtitle("kdbcgaadcf").faqs(new ArrayList<>()).build();

        when(githubGateway.getContentsAsRaw(OWNER, REPO, MASTER, FILE_PATH)).thenReturn(faqsXml);
        when(xmlMapper.readValue(faqsXml, Faqs.class)).thenReturn(Faqs.builder().pages(Arrays.asList(page1, page2, page3)).build());
        when(faqItemDtoMapper.mapToList(same(page1.getFaqs()))).thenReturn(faqItems1);
        when(faqItemDtoMapper.mapToList(same(page2.getFaqs()))).thenReturn(faqItems2);
        when(faqItemDtoMapper.mapToList(same(page3.getFaqs()))).thenReturn(faqItems3);
        when(cacheManager.getCache("faqs")).thenReturn(cache);

        service.refreshFAQs();

        verify(cache).put(eq(page1.getName()), refEq(new FaqItemsDto(page1.getSubtitle(), faqItems1)));
        verify(cache).put(eq(page2.getName()), refEq(new FaqItemsDto(page2.getSubtitle(), faqItems2)));
        verify(cache).put(eq(page3.getName()), refEq(new FaqItemsDto(page3.getSubtitle(), faqItems3.stream().filter(Objects::nonNull).collect(toList()))));
    }

    @Test
    public void refreshFAQs_pageNullFaqs() throws IOException {
        final String faqsXml = "fadgszdbg";
        final Page page1 = Page.builder().name("fgh").subtitle("jcgk").faqs(null).build();
        final Page page2 = Page.builder().name("gdx").subtitle("mvnb").faqs(new ArrayList<>()).build();
        final Page page3 = Page.builder().name("khd").subtitle("gdbh").faqs(new ArrayList<>()).build();
        final List<FaqItemDto> faqItems2 = Arrays.asList(mock(FaqItemDto.class), mock(FaqItemDto.class));
        final List<FaqItemDto> faqItems3 = Arrays.asList(mock(FaqItemDto.class), null, mock(FaqItemDto.class));
        final Cache cache = mock(Cache.class);

        when(githubGateway.getContentsAsRaw(OWNER, REPO, MASTER, FILE_PATH)).thenReturn(faqsXml);
        when(xmlMapper.readValue(faqsXml, Faqs.class)).thenReturn(Faqs.builder().pages(Arrays.asList(page1, page2, page3)).build());
        when(faqItemDtoMapper.mapToList(same(page2.getFaqs()))).thenReturn(faqItems2);
        when(faqItemDtoMapper.mapToList(same(page3.getFaqs()))).thenReturn(faqItems3);
        doThrow(new NullPointerException()).when(faqItemDtoMapper).mapToList(null);
        when(cacheManager.getCache("faqs")).thenReturn(cache);

        service.refreshFAQs();

        verify(cache).put(eq(page1.getName()), refEq(new FaqItemsDto(page1.getSubtitle(), new ArrayList<>())));
        verify(cache).put(eq(page2.getName()), refEq(new FaqItemsDto(page2.getSubtitle(), faqItems2)));
        verify(cache).put(eq(page3.getName()), refEq(new FaqItemsDto(page3.getSubtitle(), faqItems3.stream().filter(Objects::nonNull).collect(toList()))));
    }

    @Test
    public void getFAQsForPage() throws IOException {
        final String faqsXml = "fadgszdbg";
        final String pageName = "fghggfsshdg";
        final String subtitle = "afdsgdasoif";
        final List<Faq> faqs = new ArrayList<>();
        final List<FaqItemDto> faqItems = Arrays.asList(mock(FaqItemDto.class), null, mock(FaqItemDto.class));

        when(githubGateway.getContentsAsRaw(OWNER, REPO, MASTER, FILE_PATH)).thenReturn(faqsXml);
        when(xmlMapper.readValue(faqsXml, Faqs.class)).thenReturn(buildFaqsObjectWithPage(pageName, subtitle, faqs));
        when(faqItemDtoMapper.mapToList(same(faqs))).thenReturn(faqItems);

        final FaqItemsDto result = service.getFAQsForPage(pageName);

        assertThat(result.getFaqItems()).isEqualTo(faqItems.stream().filter(Objects::nonNull).collect(toList()));
        assertThat(result.getSubtitle()).isEqualTo(subtitle);
    }

    @Test
    public void getFAQsForPage_FAQsNull() throws IOException {
        final String faqsXml = "fadgszdbg";
        final String pageName = "fghggfsshdg";

        when(githubGateway.getContentsAsRaw(OWNER, REPO, MASTER, FILE_PATH)).thenReturn(faqsXml);
        when(xmlMapper.readValue(faqsXml, Faqs.class)).thenReturn(buildFaqsObjectWithPage(pageName, "", null));
        doThrow(new NullPointerException()).when(faqItemDtoMapper).mapToList(null);

        final FaqItemsDto result = service.getFAQsForPage(pageName);

        assertThat(result.getFaqItems()).isEmpty();
    }

    @Test
    public void getFAQsForPage_xmlMapperThrowsException() throws IOException {
        final String faqsXml = "vgjk";
        final String pageName = "nbn";
        final IOException ioException = new IOException();

        when(githubGateway.getContentsAsRaw(OWNER, REPO, MASTER, FILE_PATH)).thenReturn(faqsXml);
        doThrow(ioException).when(xmlMapper).readValue(faqsXml, Faqs.class);

        try {
            service.getFAQsForPage(pageName);
            fail("A RuntimeException(\"Something went wrong while trying to parse FAQ.xml\", e) was expected to be thrown");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Something went wrong while trying to parse FAQ.xml");
            assertThat(e.getCause()).isEqualTo(ioException);
        }
    }

    @Test
    public void getFAQsForPage_noFAQsFound() throws IOException {
        final String faqsXml = "fadgszdbg";
        final List<Faq> faqs = new ArrayList<>();
        final List<FaqItemDto> faqItems = new ArrayList<>();

        when(githubGateway.getContentsAsRaw(OWNER, REPO, MASTER, FILE_PATH)).thenReturn(faqsXml);
        when(xmlMapper.readValue(faqsXml, Faqs.class)).thenReturn(buildFaqsObjectWithPage("fghggfsshdg", "sfgfb", faqs));
        when(faqItemDtoMapper.mapToList(same(faqs))).thenReturn(faqItems);

        final FaqItemsDto result = service.getFAQsForPage("pageName");

        assertThat(result.getFaqItems()).isEmpty();
    }

    private Faqs buildFaqsObjectWithPage(final String pageName, final String subtitle, final List<Faq> faqs) {
        return Faqs.builder()
                   .pages(Arrays.asList(Page.builder()
                                            .name("afdsgd")
                                            .subtitle("afdsgdasoif")
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
                                            .subtitle(subtitle)
                                            .faqs(faqs)
                                            .build()))
                   .build();
    }

}
