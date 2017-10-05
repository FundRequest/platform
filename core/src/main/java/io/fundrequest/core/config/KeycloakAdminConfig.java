package io.fundrequest.core.config;

import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

    @Autowired
    private KeycloakSpringBootProperties keycloakSpringBootProperties;

    @Value("${keycloak-custom.query-users.username}")
    private String username;

    @Value("${keycloak-custom.query-users.password}")
    private String password;

    @Bean
    public RealmResource adminRealmResource() {
        Keycloak keycloak = Keycloak.getInstance(
                keycloakSpringBootProperties.getAuthServerUrl(),
                keycloakSpringBootProperties.getRealm(),
                username,
                password,
                keycloakSpringBootProperties.getResource(),
                keycloakSpringBootProperties.getCredentials().get("secret").toString());
        return keycloak.realm(keycloakSpringBootProperties.getRealm());
    }
}
