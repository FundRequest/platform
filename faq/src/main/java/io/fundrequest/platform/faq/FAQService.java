package io.fundrequest.platform.faq;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.fundrequest.platform.faq.model.FaqItem;
import io.fundrequest.platform.faq.parser.Faqs;
import io.fundrequest.platform.github.GithubGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class FAQService {

    private final GithubGateway githubGateway;
    private final String owner;
    private final String repo;
    private final String filePath;
    private final XmlMapper xmlMapper;
    private final FaqItemMapper faqItemMapper;

    public FAQService(final GithubGateway githubGateway,
                      @Value("${io.fundrequest.faq.owner:FundRequest}") final String owner,
                      @Value("${io.fundrequest.faq.repo:FAQ}") final String repo,
                      @Value("${io.fundrequest.faq.repo:FAQ.xml}") String filePath,
                      final XmlMapper xmlMapper,
                      final FaqItemMapper faqItemMapper) {
        this.githubGateway = githubGateway;
        this.owner = owner;
        this.repo = repo;
        this.filePath = filePath;
        this.xmlMapper = xmlMapper;
        this.faqItemMapper = faqItemMapper;
    }

    @Cacheable("faq")
    public List<FaqItem> getFAQsForPage(final String pageName) {
        final Faqs faqs;
        try {
            faqs = xmlMapper.readValue(githubGateway.getContentsAsRaw(owner, repo, filePath), Faqs.class);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong while trying to parse FAQ.xml", e);
        }
        return faqs.getPages()
                   .stream()
                   .filter(page -> page.getName().equalsIgnoreCase(pageName))
                   .findFirst().map(page -> faqItemMapper.mapToList(page.getFaqs()))
                   .orElseThrow(() -> new RuntimeException("No page definition for " + pageName + " found in FAQ.xml"));
    }

    @CacheEvict("faq")
    public void evictFAQs() {
        // Intentionally blank
    }
}
