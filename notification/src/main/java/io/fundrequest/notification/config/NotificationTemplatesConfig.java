package io.fundrequest.notification.config;

import io.fundrequest.platform.github.GithubRawClient;
import io.fundrequest.platform.github.resourceresolver.GithubTemplateResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class NotificationTemplatesConfig {

    @Bean
    public ITemplateResolver githubHtmlTemplateResolver(final GithubRawClient githubRawClient) {
        final GithubTemplateResolver templateResolver = new GithubTemplateResolver("FundRequest", "content-management", "master", githubRawClient);
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setOrder(Integer.MAX_VALUE);
        templateResolver.setCacheable(false);
        templateResolver.setCheckExistence(true);
        return templateResolver;
    }

    @Bean
    public ITemplateResolver githubTextTemplateResolver(final GithubRawClient githubRawClient) {
        final GithubTemplateResolver templateResolver = new GithubTemplateResolver("FundRequest", "content-management", "master", githubRawClient);
        templateResolver.setSuffix(".txt");
        templateResolver.setTemplateMode(TemplateMode.TEXT);
        templateResolver.setOrder(Integer.MAX_VALUE);
        templateResolver.setCacheable(false);
        templateResolver.setCheckExistence(true);
        return templateResolver;
    }

    @Bean
    public TemplateEngine githubTemplateEngine(final ITemplateResolver githubHtmlTemplateResolver, final ITemplateResolver githubTextTemplateResolver) {
        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.addTemplateResolver(githubHtmlTemplateResolver);
        templateEngine.addTemplateResolver(githubTextTemplateResolver);
        return templateEngine;
    }
}