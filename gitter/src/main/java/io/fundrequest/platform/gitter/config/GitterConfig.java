package io.fundrequest.platform.gitter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.platform.github.GithubRawClient;
import io.fundrequest.platform.gitter.GitterService;
import io.fundrequest.platform.gitter.RequestFundedGitterHandler;
import io.fundrequest.social.gitter.api.Gitter;
import io.fundrequest.social.gitter.connect.GitterConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.Connection;
import org.springframework.social.oauth2.AccessGrant;
import org.thymeleaf.ITemplateEngine;

@Configuration
@ConditionalOnProperty(name = "io.fundrequest.notifications.gitter.enabled", havingValue = "true")
public class GitterConfig {

    @Bean
    public Gitter gitter(@Value("${io.fundrequest.notifications.gitter.consumer-key}") final String consumerKey,
                         @Value("${io.fundrequest.notifications.gitter.consumer-secret}") final String consumerSecret,
                         @Value("${io.fundrequest.notifications.gitter.access-token}") final String accessToken) {
        final Connection<Gitter> gitterConnection = new GitterConnectionFactory(consumerKey, consumerSecret).createConnection(new AccessGrant(accessToken));
        assert gitterConnection.test();
        return gitterConnection.getApi();
    }

    @Bean
    public GitterService gitterService(final GithubRawClient githubRawClient,
                                       @Value("${io.fundrequest.notifications.gitter.rooms.branch}") final String branch,
                                       @Value("${io.fundrequest.notifications.gitter.rooms.path}") final String path,
                                       final ObjectMapper objectMapper) {
        return new GitterService(githubRawClient, branch, path, objectMapper);

    }

    @Bean
    public RequestFundedGitterHandler requestFundedGitterHandler(final Gitter gitter,
                                                                 final ITemplateEngine githubTemplateEngine,
                                                                 final FundService fundService,
                                                                 @Value("${io.fundrequest.platform.base-path}") final String platformBasePath,
                                                                 final GitterService gitterService) {
        return new RequestFundedGitterHandler(gitter, githubTemplateEngine, fundService, platformBasePath, gitterService);

    }
}
