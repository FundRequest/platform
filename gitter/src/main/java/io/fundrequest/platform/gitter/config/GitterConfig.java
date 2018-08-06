package io.fundrequest.platform.gitter.config;

import io.fundrequest.social.gitter.api.Gitter;
import io.fundrequest.social.gitter.connect.GitterConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.Connection;
import org.springframework.social.oauth2.AccessGrant;

@Configuration
@ConditionalOnProperty(name = "io.fundrequest.notifications.gitter.enabled", havingValue = "true")
public class GitterConfig {

    @Bean
    public Gitter gitter(final Connection<Gitter> gitterConnection) {
        assert gitterConnection.test();
        return gitterConnection.getApi();
    }

    @Bean
    public Connection<Gitter> gitterConnection(final GitterConnectionFactory gitterConnectionFactory,
                                               @Value("${io.fundrequest.notifications.gitter.access-token}") final String accessToken) {
        return gitterConnectionFactory.createConnection(new AccessGrant(accessToken));
    }

    @Bean
    public GitterConnectionFactory gitterConnectionFactory(@Value("${io.fundrequest.notifications.gitter.consumer-key}") final String consumerKey,
                                                           @Value("${io.fundrequest.notifications.gitter.consumer-secret}") final String consumerSecret) {
        return new GitterConnectionFactory(consumerKey, consumerSecret);
    }
}
