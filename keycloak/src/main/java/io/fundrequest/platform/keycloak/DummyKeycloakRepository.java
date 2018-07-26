package io.fundrequest.platform.keycloak;

import lombok.NonNull;
import org.apache.commons.lang3.BooleanUtils;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@Profile("local")
class DummyKeycloakRepository implements KeycloakRepository {


    private static final String ETHER_ADDRESS_KEY = "ether_address";
    private static final String ETHER_ADDRESS_VERIFIED_KEY = "ether_address_verified";
    private static final String VERIFIED_DEVELOPER_KEY = "verified_developer";
    private static final String TELEGRAM_NAME_KEY = "telegram_name";
    private static final String HEADLINE_KEY = "headline";
    private static final String PICTURE_KEY = "picture";
    private final String githubUsername;
    private UserRepresentation userRepresentation;

    public DummyKeycloakRepository(@Value("${feign.client.github.username}") final String githubUsername,
                                   @Value("${local.ethereum.kovan.address:0x0}") final String etherAddress) {
        this.githubUsername = githubUsername;

        userRepresentation = new UserRepresentation();
        userRepresentation.setCreatedTimestamp(new Date().getTime());
        userRepresentation.setEmail("john.doe@mail.com");
        userRepresentation.setFirstName("John");
        userRepresentation.setLastName("Doe");
        userRepresentation.setEnabled(true);
        userRepresentation.setUsername("johndoe");
        userRepresentation.setEmailVerified(true);
        userRepresentation.setAttributes(new HashMap<>());

        updateEtherAddress("", etherAddress);
        updateHeadline("", "CEO");
        updateTelegramName("", "telegram");
        updateVerifiedDeveloper("", true);
    }

    public Stream<UserIdentity> getUserIdentities(String userId) {
        return Stream.of(UserIdentity.builder()
                                     .provider(Provider.GITHUB)
                                     .username(githubUsername)
                                     .userId("johndoeID")
                                     .build());
    }


    public UserRepresentation getUser(String userId) {
        userRepresentation.setId(userId);
        return userRepresentation;
    }


    public void updateEtherAddress(String userId, String etherAddress) {
        updateAttribute(ETHER_ADDRESS_KEY, etherAddress);
    }

    @Override
    public void updateEtherAddressVerified(String userId, Boolean isVerified) {
        updateAttribute(ETHER_ADDRESS_VERIFIED_KEY, String.valueOf(BooleanUtils.isTrue(isVerified)));
    }

    public void updateTelegramName(String userId, String telegramName) {
        updateAttribute(TELEGRAM_NAME_KEY, telegramName);
    }

    public void updateHeadline(String userId, String headline) {
        updateAttribute(HEADLINE_KEY, headline);
    }

    public void updateVerifiedDeveloper(String userId, Boolean isVerified) {

        updateAttribute(VERIFIED_DEVELOPER_KEY, "" + BooleanUtils.isTrue(isVerified));
    }

    private void updateAttribute(String property, String value) {
        if (userRepresentation.getAttributes() == null) {
            userRepresentation.setAttributes(new HashMap<>());
        }
        userRepresentation.getAttributes().put(property, Collections.singletonList(value));
    }

    public String getEtherAddress(String userId) {
        return getAttribute(ETHER_ADDRESS_KEY);
    }

    public String getEtherAddress(UserRepresentation userRepresentation) {
        return getAttribute(ETHER_ADDRESS_KEY);
    }

    @Override
    public boolean isEtherAddressVerified(UserRepresentation userRepresentation) {
        return "true".equalsIgnoreCase(getAttribute(ETHER_ADDRESS_VERIFIED_KEY));
    }

    public boolean isVerifiedDeveloper(UserRepresentation userRepresentation) {
        return "true".equalsIgnoreCase(getAttribute(VERIFIED_DEVELOPER_KEY));
    }

    public boolean isVerifiedDeveloper(final String userId) {
        return "true".equalsIgnoreCase(getAttribute(VERIFIED_DEVELOPER_KEY));
    }

    @Override
    public String getAttribute(UserRepresentation userRepresentation, String property) {
        return getAttribute(property);
    }

    public String getAttribute(String property) {
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes != null && attributes.size() > 0) {
            List<String> properties = attributes.get(property);
            if (!CollectionUtils.isEmpty(properties)) {
                return properties.get(0);
            }
        }
        return null;
    }

    public String getTelegramName(UserRepresentation userRepresentation) {
        return getAttribute(TELEGRAM_NAME_KEY);
    }

    public String getPicture(UserRepresentation userRepresentation) {
        return getAttribute(PICTURE_KEY);
    }

    public String getHeadline(UserRepresentation userRepresentation) {
        return getAttribute(HEADLINE_KEY);
    }

    public String getAccessToken(@NonNull KeycloakAuthenticationToken token, @NonNull Provider provider) {
        return null;
    }


    public boolean userExists(String userId) {
        return true;
    }
}
