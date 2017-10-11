package io.fundrequest.core.config;


import org.junit.Ignore;
import org.junit.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;

public class KeycloakAdminConfigTest {

    @Test
    @Ignore
    public void fsdfsqd() throws Exception {
        Keycloak keycloak = Keycloak.getInstance(
                "https://dev-key.fundrequest.io/auth",
                "fundrequest",
                "fundrequest-query-users",
                "password",
                "fundrequest_dev",
                "secret");
        RealmResource realm = keycloak.realm("fundrequest");
        realm.users().count();
    }
}