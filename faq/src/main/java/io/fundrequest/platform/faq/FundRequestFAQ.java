package io.fundrequest.platform.faq;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import io.fundrequest.common.infrastructure.IgnoreDuringComponentScan;
import io.fundrequest.platform.github.GithubGateway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableConfigurationProperties()
@ComponentScan(
        basePackageClasses = {FundRequestFAQ.class,},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class),
                @ComponentScan.Filter(IgnoreDuringComponentScan.class)})
public class FundRequestFAQ {


    @Bean
    @ConditionalOnProperty("io.fundrequest.faq.enabled")
    public FAQService faqService(final GithubGateway githubGateway,
                                 @Value("${io.fundrequest.faq.owner:FundRequest}") final String owner,
                                 @Value("${io.fundrequest.faq.repo:FAQ}") final String repo,
                                 @Value("${io.fundrequest.faq.branch:master}") final String branch,
                                 @Value("${io.fundrequest.faq.repo:FAQ.xml}") final String filePath,
                                 final XmlMapper xmlMapper,
                                 final FaqItemDtoMapper faqItemDtoMapper,
                                 final CacheManager cacheManager) {
        return new GithubFAQServiceImpl(githubGateway, owner, repo, branch, filePath, xmlMapper, faqItemDtoMapper, cacheManager);
    }

    @Bean
    @ConditionalOnProperty(name = "io.fundrequest.faq.enabled", havingValue = "false")
    public FAQService emptyFaqService() {
        return new EmptyFAQServiceImpl();
    }
}
