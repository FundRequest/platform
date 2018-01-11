package io.fundrequest.core.keycloak;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakConfig {


    @Bean
    public RealmResource keycloak(@Value("${io.fundrequest.keycloak-custom.server-url}") String serverUrl,
                                  @Value("${io.fundrequest.keycloak-custom.realm}") String realm,
                                  @Value("${io.fundrequest.keycloak-custom.username}") String username,
                                  @Value("${io.fundrequest.keycloak-custom.password}") String password,
                                  @Value("${io.fundrequest.keycloak-custom.client-id}") String clientId,
                                  @Value("${io.fundrequest.keycloak-custom.client-secret}") String clientSecret) {
        Keycloak keycloak = Keycloak.getInstance(
                serverUrl,
                realm,
                username,
                password,
                clientId,
                clientSecret);
        return keycloak.realm(realm);
    }
}
