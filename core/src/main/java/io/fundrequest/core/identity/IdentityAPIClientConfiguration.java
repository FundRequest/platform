package io.fundrequest.core.identity;

import feign.auth.BasicAuthRequestInterceptor;
import io.fundrequest.common.infrastructure.IgnoreDuringComponentScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@IgnoreDuringComponentScan
public class IdentityAPIClientConfiguration {


    @Bean
    @ConditionalOnProperty(name = {"io.fundrequest.identity.api.user.name", "io.fundrequest.identity.api.user.password"})
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(@Value("${io.fundrequest.identity.api.user.name}") final String username,
                                                                   @Value("${io.fundrequest.identity.api.user.password}") final String password) {
        return new BasicAuthRequestInterceptor(username, password);
    }
}
