package io.fundrequest.platform.intercom;

import io.fundrequest.common.infrastructure.IgnoreDuringComponentScan;
import io.fundrequest.common.infrastructure.feign.BearerAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@IgnoreDuringComponentScan
public class IntercomFeignConfiguration {

    @Bean
    @ConditionalOnProperty(name = {"feign.client.intercom.access_token"})
    public BearerAuthRequestInterceptor bearerAuthRequestInterceptor(@Value("${feign.client.intercom.access_token}") String accessToken) {
        return new BearerAuthRequestInterceptor(accessToken);
    }
}
