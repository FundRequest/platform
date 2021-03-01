package io.fundrequest.platform.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import static org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS;

@Configuration
@Profile("!local")
public class KeycloakConfig {

    @Value("${keycloak.auth-server-url}")
    private String serverUrl;
    @Value("${io.fundrequest.keycloak-custom.realm}")
    private String realm;
    @Value("${io.fundrequest.keycloak-custom.client-id}")
    private String clientId;
    @Value("${io.fundrequest.keycloak-custom.client-secret}")
    private String clientSecret;

    @Bean
    public RealmResource keycloak() {
        Keycloak keycloak = KeycloakBuilder.builder()
                                           .serverUrl(serverUrl)
                                           .realm(realm)
                                           .grantType(CLIENT_CREDENTIALS)
                                           .clientId(clientId)
                                           .clientSecret(clientSecret)
                                           .build();

        return keycloak.realm(realm);
    }
}
