package io.fundrequest.platform.keycloak;

import lombok.NonNull;
import org.apache.commons.lang3.BooleanUtils;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@Profile("local")
class DummyKeycloakRepository implements KeycloakRepository {


    private final String githubUsername;
    private UserRepresentation userRepresentation;

    public DummyKeycloakRepository(@Value("${local.github.username}") final String githubUsername,
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
        updateAttribute("ether_address", etherAddress);
    }

    public void updateTelegramName(String userId, String telegramName) {
        updateAttribute("telegram_name", telegramName);
    }

    public void updateHeadline(String userId, String headline) {
        updateAttribute("headline", headline);
    }

    public void updateVerifiedDeveloper(String userId, Boolean isVerified) {

        updateAttribute("verified_developer", "" + BooleanUtils.isTrue(isVerified));
    }

    private void updateAttribute(String property, String value) {
        if (userRepresentation.getAttributes() == null) {
            userRepresentation.setAttributes(new HashMap<>());
        }
        userRepresentation.getAttributes().put(property, Collections.singletonList(value));
    }

    public String getEtherAddress(String userId) {
        return getAttribute("ether_address");
    }

    public String getEtherAddress(UserRepresentation userRepresentation) {
        return getAttribute("ether_address");
    }

    public boolean isVerifiedDeveloper(UserRepresentation userRepresentation) {
        return "true".equalsIgnoreCase(getAttribute("verified_developer"));
    }

    public boolean isVerifiedDeveloper(final String userId) {
        return "true".equalsIgnoreCase(getAttribute("verified_developer"));
    }

    @Override
    public String getAttribute(UserRepresentation userRepresentation, String property) {
        return getAttribute(property);
    }

    public String getAttribute(String property) {
        Map<String, List<String>> attributes = userRepresentation.getAttributes();
        if (attributes != null && attributes.size() > 0) {
            List<String> properties = attributes.get(property);
            if (properties != null && properties.size() > 0) {
                return properties.get(0);
            }
        }
        return null;
    }

    public String getTelegramName(UserRepresentation userRepresentation) {
        return getAttribute("telegram_name");
    }

    public String getPicture(UserRepresentation userRepresentation) {
        return getAttribute("picture");
    }

    public String getHeadline(UserRepresentation userRepresentation) {
        return getAttribute("headline");
    }

    public String getAccessToken(@NonNull KeycloakAuthenticationToken token, @NonNull Provider provider) {
        return null;
    }


    public boolean userExists(String userId) {
        return true;
    }
}
