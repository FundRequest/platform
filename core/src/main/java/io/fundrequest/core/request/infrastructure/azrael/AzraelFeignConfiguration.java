package io.fundrequest.core.request.infrastructure.azrael;


import feign.auth.BasicAuthRequestInterceptor;
import io.fundrequest.common.infrastructure.IgnoreDuringComponentScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@IgnoreDuringComponentScan
public class AzraelFeignConfiguration {

    @Bean
    @ConditionalOnProperty(name = {
            "feign.client.azrael.username", "feign.client.azrael.password"
    })
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(
            @Value("${feign.client.azrael.username}") final String azraelUsername,
            @Value("${feign.client.azrael.password}") final String azraelPassword) {
        return new BasicAuthRequestInterceptor(azraelUsername, azraelPassword);
    }

}
