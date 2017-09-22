package io.fundrequest.core.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

    @Autowired
    private KeycloakSpringBootProperties keycloakSpringBootProperties;

    @Bean
    public RealmResource adminRealmResource() {
        Keycloak keycloak = Keycloak.getInstance(
                keycloakSpringBootProperties.getAuthServerUrl(),
                keycloakSpringBootProperties.getRealm(),
                "fundrequest-query-users",
                "Underthecloakofnight",
                keycloakSpringBootProperties.getResource(),
                keycloakSpringBootProperties.getCredentials().get("secret").toString());
        return keycloak.realm(keycloakSpringBootProperties.getRealm());
    }
}
