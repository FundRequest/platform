package io.fundrequest.platform.keycloak;

import org.assertj.core.api.Java6Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class KeycloakRepositoryImplTest {

    private static final String ETHER_ADDRESS_KEY = "ether_address";
    private static final String ETHER_ADDRESS_VERIFIED_KEY = "ether_address_verified";

    private RealmResource realmResource;
    private KeycloakRepository keycloakRepository;

    @BeforeEach
    public void setUp() {
        realmResource = mock(RealmResource.class, RETURNS_DEEP_STUBS);
        keycloakRepository = new KeycloakRepositoryImpl(realmResource, "url");
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

        Java6Assertions.assertThat(result.collect(Collectors.toList())).containsExactly(
                UserIdentity.builder()
                        .provider(Provider.GITHUB)
                        .userId(userId)
                        .username("davyvanroy")
                        .build());
    }
    
    @Test
    void updateEtherAddress() {
        final String userId = "agd";
        final UserResource userResource = mock(UserResource.class);
        final UserRepresentation userRepresentation = mock(UserRepresentation.class);
        final Map<String, List<String>> userAttributes = new HashMap<>();
        userAttributes.put(ETHER_ADDRESS_KEY, Collections.singletonList("0x24ace"));
        userAttributes.put(ETHER_ADDRESS_VERIFIED_KEY, Collections.singletonList("true"));

        when(realmResource.users().get(userId)).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(userRepresentation);
        when(userRepresentation.getAttributes()).thenReturn(userAttributes);

        keycloakRepository.updateEtherAddress(userId, "0x13bdf");

        assertThat(userAttributes.get(ETHER_ADDRESS_KEY)).isEqualTo(Collections.singletonList("0x13bdf"));
        assertThat(userAttributes.get(ETHER_ADDRESS_VERIFIED_KEY)).isEqualTo(Collections.singletonList("false"));
        verify(userResource).update(userRepresentation);
    }

    @Test
    void updateEtherAddress_doNotUpdateWhenSameAddress() {
        final String userId = "agd";
        final UserResource userResource = mock(UserResource.class);
        final UserRepresentation userRepresentation = mock(UserRepresentation.class);
        final Map<String, List<String>> userAttributes = new HashMap<>();
        final String address = "0x24ace";
        userAttributes.put(ETHER_ADDRESS_KEY, Collections.singletonList(address));
        userAttributes.put(ETHER_ADDRESS_VERIFIED_KEY, Collections.singletonList("true"));

        when(realmResource.users().get(userId)).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(userRepresentation);
        when(userRepresentation.getAttributes()).thenReturn(userAttributes);

        keycloakRepository.updateEtherAddress(userId, address);

        assertThat(userAttributes.get(ETHER_ADDRESS_KEY)).isEqualTo(Collections.singletonList(address));
        assertThat(userAttributes.get(ETHER_ADDRESS_VERIFIED_KEY)).isEqualTo(Collections.singletonList("true"));
        verify(userResource, never()).update(userRepresentation);
    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "false"})
    void updateVerifiedEtherAddress(final String isVerifiedAsString) {
        verifyAttributeUpdate((userId1, value) -> keycloakRepository.updateEtherAddressVerified(userId1, Boolean.valueOf(value)),
                              "634",
                              ETHER_ADDRESS_VERIFIED_KEY,
                              isVerifiedAsString);

    }

    @ParameterizedTest
    @ValueSource(strings = {"true", "false"})
    public void isVerifiedEtherAddress(final String isVerifiedAsString) {
        final UserRepresentation userRepresentation = mock(UserRepresentation.class);
        final Map<String, List<String>> userAttributes = new HashMap<>();
        userAttributes.put(ETHER_ADDRESS_VERIFIED_KEY, Collections.singletonList(isVerifiedAsString));

        when(userRepresentation.getAttributes()).thenReturn(userAttributes);

        final boolean result = keycloakRepository.isEtherAddressVerified(userRepresentation);

        assertThat(result).isEqualTo(Boolean.valueOf(isVerifiedAsString));
    }

    @Test
    public void isVerifiedEtherAddress_attribute_not_present() {
        final UserRepresentation userRepresentation = mock(UserRepresentation.class);
        final Map<String, List<String>> userAttributes = new HashMap<>();
        userAttributes.put("djghfh", new ArrayList<>());

        when(userRepresentation.getAttributes()).thenReturn(userAttributes);

        final boolean result = keycloakRepository.isEtherAddressVerified(userRepresentation);

        assertThat(result).isFalse();
    }

    @Test
    public void isVerifiedEtherAddress_attributes_empty() {
        final UserRepresentation userRepresentation = mock(UserRepresentation.class);
        final Map<String, List<String>> userAttributes = new HashMap<>();

        when(userRepresentation.getAttributes()).thenReturn(userAttributes);

        final boolean result = keycloakRepository.isEtherAddressVerified(userRepresentation);

        assertThat(result).isFalse();
    }

    @Test
    public void isVerifiedEtherAddress_attributes_null() {
        final UserRepresentation userRepresentation = mock(UserRepresentation.class);
        final Map<String, List<String>> userAttributes = null;

        when(userRepresentation.getAttributes()).thenReturn(userAttributes);

        final boolean result = keycloakRepository.isEtherAddressVerified(userRepresentation);

        assertThat(result).isFalse();
    }

    private void verifyAttributeUpdate(final BiConsumer<String, String> methodToTest, final String userId, final String key, final String value) {
        final UserResource userResource = mock(UserResource.class);
        final UserRepresentation userRepresentation = mock(UserRepresentation.class);
        final Map<String, List<String>> userAttributes = new HashMap<>();

        when(realmResource.users().get(userId)).thenReturn(userResource);
        when(userResource.toRepresentation()).thenReturn(userRepresentation);
        when(userRepresentation.getAttributes()).thenReturn(userAttributes);

        methodToTest.accept(userId, value);

        assertThat(userAttributes.get(key)).isEqualTo(Collections.singletonList(value));
        verify(userResource).update(userRepresentation);
    }
}
