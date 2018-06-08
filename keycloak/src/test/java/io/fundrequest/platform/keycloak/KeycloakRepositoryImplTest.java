package io.fundrequest.platform.keycloak;

import org.assertj.core.api.Java6Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.mockito.Mockito;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeycloakRepositoryImplTest {

    private RealmResource realmResource;
    private KeycloakRepository keycloakRepository;

    @BeforeEach
    public void setUp() throws Exception {
        realmResource = Mockito.mock(RealmResource.class, Mockito.RETURNS_DEEP_STUBS);
        keycloakRepository = new KeycloakRepositoryImpl(realmResource, "url");
    }

    @Test
    public void getUserIdentities() {
        String userId = "123";
        FederatedIdentityRepresentation fi = new FederatedIdentityRepresentation();
        fi.setUserId(userId);
        fi.setUserName("davyvanroy");
        fi.setIdentityProvider("GITHUB");
        Mockito.when(realmResource.users().get(userId).getFederatedIdentity()
                                  .stream()).thenReturn(Stream.of(fi));

        Stream<UserIdentity> result = keycloakRepository.getUserIdentities(userId);

        Java6Assertions.assertThat(result.collect(Collectors.toList())).containsExactly(
                UserIdentity.builder()
                        .provider(Provider.GITHUB)
                        .userId(userId)
                        .username("davyvanroy")
                        .build());
    }
}
