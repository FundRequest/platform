package io.fundrequest.platform.keycloak;

import lombok.NonNull;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.stream.Stream;

public interface KeycloakRepository {

    Stream<UserIdentity> getUserIdentities(String userId);

    UserRepresentation getUser(String userId);

    void updateEtherAddress(String userId, String etherAddress);

    void updateEtherAddressVerified(String userId, Boolean isVerified);

    void updateTelegramName(String userId, String telegramName);

    void updateHeadline(String userId, String headline);

    void updateVerifiedDeveloper(String userId, Boolean isVerified);

    String getEtherAddress(String userId);

    String getEtherAddress(UserRepresentation userRepresentation);

    boolean isEtherAddressVerified(UserRepresentation userRepresentation);

    boolean isVerifiedDeveloper(UserRepresentation userRepresentation);

    boolean isVerifiedDeveloper(final String userId);

    String getAttribute(UserRepresentation userRepresentation, String property);

    String getTelegramName(UserRepresentation userRepresentation);

    String getPicture(UserRepresentation userRepresentation);

    String getHeadline(UserRepresentation userRepresentation);

    String getAccessToken(@NonNull KeycloakAuthenticationToken token, @NonNull Provider provider);

    boolean userExists(String userId);
}
