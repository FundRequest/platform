package io.fundrequest.platform.faq;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.fundrequest.platform.faq.model.FaqItemDto;
import io.fundrequest.platform.faq.model.FaqItemsDto;
import io.fundrequest.platform.faq.parser.Faq;
import io.fundrequest.platform.faq.parser.Faqs;
import io.fundrequest.platform.github.GithubGateway;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

public class GithubFAQServiceImpl implements FAQService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GithubFAQServiceImpl.class);
    private static final int DAY_IN_MILLIS = 86400000;

    private final GithubGateway githubGateway;
    private final String owner;
    private final String repo;
    private final String branch;
    private final String filePath;
    private final XmlMapper xmlMapper;
    private final FaqItemDtoMapper faqItemDtoMapper;
    private final CacheManager cacheManager;

    public GithubFAQServiceImpl(final GithubGateway githubGateway,
                                @Value("${io.fundrequest.faq.owner:FundRequest}") final String owner,
                                @Value("${io.fundrequest.faq.repo:FAQ}") final String repo,
                                @Value("${io.fundrequest.faq.branch:master}") final String branch,
                                @Value("${io.fundrequest.faq.filePath:FAQ.xml}") final String filePath,
                                final XmlMapper xmlMapper,
                                final FaqItemDtoMapper faqItemDtoMapper,
                                final CacheManager cacheManager) {
        LOGGER.info("GithubFAQServiceImpl is configured to be used");
        this.githubGateway = githubGateway;
        this.owner = owner;
        this.repo = repo;
        this.branch = branch;
        this.filePath = filePath;
        this.xmlMapper = xmlMapper;
        this.faqItemDtoMapper = faqItemDtoMapper;
        this.cacheManager = cacheManager;
    }

    @Scheduled(fixedRate = DAY_IN_MILLIS)
    public void refreshFAQs() {
        fetchFAQs().getPages()
                   .forEach(page -> cacheManager.getCache("faqs")
                                                .put(page.getName(), processFAQs(page.getFaqs(), page.getSubtitle())));
        LOGGER.info("FAQ's are fetched from GitHub and stored in cache");
    }

    @Cacheable(cacheNames = "faqs", key = "#pageName")
    public FaqItemsDto getFAQsForPage(final String pageName) {
        // This method is a fallback in case refreshFAQs failed or didn't ran for some reason
        return fetchFAQs().getPages()
                          .stream()
                          .filter(page -> page.getName().equalsIgnoreCase(pageName))
                          .findFirst().map(page -> processFAQs(page.getFaqs(), page.getSubtitle()))
                          .orElse(new FaqItemsDto(StringUtils.EMPTY, new ArrayList<>()));
    }

    private Faqs fetchFAQs() {
        try {
            return xmlMapper.readValue(githubGateway.getContentsAsRaw(owner, repo, branch, filePath), Faqs.class);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong while trying to parse FAQ.xml", e);
        }
    }

    private FaqItemsDto processFAQs(List<Faq> faqs, String subtitle) {
        FaqItemsDto faq = new FaqItemsDto(subtitle, new ArrayList<>());

        if (faqs != null) {
            faq.setFaqItems(filterNulls(faqItemDtoMapper.mapToList(faqs)));
        }

        return faq;
    }

    private List<FaqItemDto> filterNulls(final List<FaqItemDto> faqItems) {
        return faqItems.stream().filter(Objects::nonNull).collect(toList());
    }
}
