package io.fundrequest.core.config;

import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.httpclient.ApacheHttpClient;
import io.fundrequest.core.identity.IdentityAPIClient;
import io.fundrequest.core.identity.IdentityAPIClientDummy;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import( {FeignClientsConfiguration.class})
public class IdentityApiConfig {

    @Bean
    @ConditionalOnProperty(value = "io.fundrequest.mock.identity-api", havingValue = "false", matchIfMissing = true)
    public Client identityApiFeignClient(@Autowired(required = false) final HttpClient httpClient) {
        if (httpClient != null) {
            return new ApacheHttpClient(httpClient);
        }
        return new ApacheHttpClient();
    }

    @Bean
    @ConditionalOnProperty(value = "io.fundrequest.mock.identity-api", havingValue = "false", matchIfMissing = true)
    public IdentityAPIClient identityAPIClient(final Decoder decoder,
                                               final Encoder encoder,
                                               final Client identityApiFeignClient,
                                               final Contract contract,
                                               @Value("${io.fundrequest.identity.api.url}") final String url,
                                               @Value("${io.fundrequest.identity.api.user.name}") final String username,
                                               @Value("${io.fundrequest.identity.api.user.password}") final String password) {
        return Feign.builder()
                    .client(identityApiFeignClient)
                    .encoder(encoder)
                    .decoder(decoder)
                    .contract(contract)
                    .requestInterceptor(new BasicAuthRequestInterceptor(username, password))
                    .target(IdentityAPIClient.class, url);
    }

    @Bean
    @ConditionalOnProperty(value = "io.fundrequest.mock.identity-api", havingValue = "true")
    public IdentityAPIClient mockIdentityAPIClient() {
        return new IdentityAPIClientDummy();
    }
}
