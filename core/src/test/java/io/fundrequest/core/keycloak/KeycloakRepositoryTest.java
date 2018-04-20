package io.fundrequest.core.keycloak;

import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.keycloak.UserIdentity;
import org.junit.Before;
import org.junit.Test;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KeycloakRepositoryTest {

    private RealmResource realmResource;
    private KeycloakRepository keycloakRepository;

    @Before
    public void setUp() throws Exception {
        realmResource = mock(RealmResource.class, RETURNS_DEEP_STUBS);
        keycloakRepository = new KeycloakRepository(realmResource, "url");
    }

    @Test
    public void getUserIdentities() {
        String userId = "123";
        FederatedIdentityRepresentation fi = new FederatedIdentityRepresentation();
        fi.setUserId(userId);
        fi.setUserName("davyvanroy");
        fi.setIdentityProvider("GITHUB");
        when(realmResource.users().get(userId).getFederatedIdentity()
                .stream()).thenReturn(Stream.of(fi));

        Stream<UserIdentity> result = keycloakRepository.getUserIdentities(userId);

        assertThat(result.collect(Collectors.toList())).containsExactly(
                UserIdentity.builder()
                        .provider(Provider.GITHUB)
                        .userId(userId)
                        .username("davyvanroy")
                        .build());
    }
}