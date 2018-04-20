package io.fundrequest.platform.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {

    @Value("${io.fundrequest.keycloak-custom.server-url}")
    private String serverUrl;
    @Value("${io.fundrequest.keycloak-custom.realm}")
    private String realm;
    @Value("${io.fundrequest.keycloak-custom.username}")
    private String username;
    @Value("${io.fundrequest.keycloak-custom.password}")
    private String password;
    @Value("${io.fundrequest.keycloak-custom.client-id}")
    private String clientId;
    @Value("${io.fundrequest.keycloak-custom.client-secret}")
    private String clientSecret;

    @Bean
    public RealmResource keycloak() {
        final Keycloak keycloak = Keycloak.getInstance(
                serverUrl,
                realm,
                username,
                password,
                clientId,
                clientSecret);
        return keycloak.realm(realm);
    }
}
