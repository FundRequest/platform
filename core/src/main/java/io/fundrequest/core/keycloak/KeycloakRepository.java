package io.fundrequest.core.keycloak;

import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class KeycloakRepository {

    private RealmResource resource;

    public KeycloakRepository(RealmResource resource) {
        this.resource = resource;
    }

    public Stream<UserIdentity> getUserIdentities(String userId) {
        return resource.users().get(userId).getFederatedIdentity()
                .stream()
                .map(fi -> new UserIdentity(fi.getIdentityProvider(), fi.getUserName()));
    }
}
