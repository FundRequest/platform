package io.fundrequest.core.request.infrastructure.github;

import feign.auth.BasicAuthRequestInterceptor;
import io.fundrequest.core.infrastructure.IgnoreDuringComponentScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@IgnoreDuringComponentScan
public class GithubFeignConfiguration {

    @Bean
    @ConditionalOnProperty(name = {
            "feign.client.github.username", "feign.client.github.password"
    })
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(
            @Value("${feign.client.github.username}") String githubUsername,
            @Value("${feign.client.github.password}") String githubPassword
    ) {
        return new BasicAuthRequestInterceptor(githubUsername, githubPassword);
    }
}
