package io.fundrequest.platform.keycloak;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.platform.keycloak.dto.LinkedInTokenResult;
import lombok.NonNull;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
@Profile("!local")
class KeycloakRepositoryImpl implements KeycloakRepository {

    private static final String ETHER_ADDRESS_KEY = "ether_address";
    private static final String ETHER_ADDRESS_VERIFIED_KEY = "ether_address_verified";
    private static final String TELEGRAM_NAME_KEY = "telegram_name";
    private static final String HEADLINE_KEY = "headline";
    private static final String VERIFIED_DEVELOPER_KEY = "verified_developer";
    private final ObjectMapper objectMapper;
    private RealmResource resource;
    private String keycloakUrl;

    public KeycloakRepositoryImpl(RealmResource resource, @Value("${keycloak.auth-server-url}") String keycloakUrl) {
        this.resource = resource;
        this.keycloakUrl = keycloakUrl;
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Stream<UserIdentity> getUserIdentities(String userId) {
        return resource.users().get(userId).getFederatedIdentity()
                       .stream()
                       .map(fi -> UserIdentity.builder().provider(Provider.fromString(fi.getIdentityProvider())).username(fi.getUserName()).userId(fi.getUserId()).build());
    }


    public UserRepresentation getUser(String userId) {
        return resource.users().get(userId).toRepresentation();
    }


    public void updateEtherAddress(String userId, String etherAddress) {
        updateAttribute(resource.users().get(userId), ETHER_ADDRESS_KEY, etherAddress);
    }

    @Override
    public void updateEtherAddressVerified(final String userId, final Boolean isVerified) {
        updateAttribute(resource.users().get(userId), ETHER_ADDRESS_VERIFIED_KEY, String.valueOf(BooleanUtils.isTrue(isVerified)));
    }

    public void updateTelegramName(String userId, String telegramName) {
        updateAttribute(resource.users().get(userId), TELEGRAM_NAME_KEY, telegramName);
    }

    public void updateHeadline(String userId, String headline) {
        updateAttribute(resource.users().get(userId), HEADLINE_KEY, headline);
    }

    public void updateVerifiedDeveloper(String userId, Boolean isVerified) {
        updateAttribute(resource.users().get(userId), VERIFIED_DEVELOPER_KEY, "" + BooleanUtils.isTrue(isVerified));
    }

    private void updateAttribute(UserResource userResource, String property, String value) {
        UserRepresentation userRepresentation = userResource.toRepresentation();
        if (userRepresentation.getAttributes() == null) {
            userRepresentation.setAttributes(new HashMap<>());
        }
        userRepresentation.getAttributes().put(property, Collections.singletonList(value));
        userResource.update(userRepresentation);
    }

    public String getEtherAddress(String userId) {
        return getAttribute(getUser(userId), ETHER_ADDRESS_KEY);
    }

    public String getEtherAddress(UserRepresentation userRepresentation) {
        return getAttribute(userRepresentation, ETHER_ADDRESS_KEY);
    }

    @Override
    public boolean isEtherAddressVerified(final UserRepresentation userRepresentation) {
        return "true".equalsIgnoreCase(getAttribute(userRepresentation, ETHER_ADDRESS_VERIFIED_KEY));
    }

    public boolean isVerifiedDeveloper(UserRepresentation userRepresentation) {
        return "true".equalsIgnoreCase(getAttribute(userRepresentation, VERIFIED_DEVELOPER_KEY));
    }

    public boolean isVerifiedDeveloper(final String userId) {
        return isVerifiedDeveloper(getUser(userId));
    }

    public String getAttribute(UserRepresentation userRepresentation, String property) {
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
        return getAttribute(userRepresentation, TELEGRAM_NAME_KEY);
    }

    public String getPicture(UserRepresentation userRepresentation) {
        return getAttribute(userRepresentation, "picture");
    }

    public String getHeadline(UserRepresentation userRepresentation) {
        return getAttribute(userRepresentation, HEADLINE_KEY);
    }

    public String getAccessToken(@NonNull KeycloakAuthenticationToken token, @NonNull Provider provider) {
        return getProfileAccessToken(token, provider);
    }

    private String getProfileAccessToken(@NonNull KeycloakAuthenticationToken token, @NonNull Provider provider) {
        HttpClient httpclient = HttpClientBuilder.create().build();  // the http-client, that will send the request
        HttpGet httpGet = new HttpGet(keycloakUrl + "/realms/fundrequest/broker/" + provider.name().toLowerCase() + "/token");   // the http GET request
        httpGet.addHeader("Authorization", "Bearer " + token.getAccount().getKeycloakSecurityContext().getTokenString());
        try {
            HttpResponse response = httpclient.execute(httpGet);
            if (response.getStatusLine().getStatusCode() != 200) {
                throw new RuntimeException("An error occurred when contacting IDP");
            }
            return getProviderAccessToken(provider, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getProviderAccessToken(Provider provider, HttpResponse response) throws IOException {
        if (provider == Provider.LINKEDIN) {
            return objectMapper.readValue(EntityUtils.toString(response.getEntity()), LinkedInTokenResult.class).getAccessToken();
        }
        throw new RuntimeException("not supported");

    }

    public boolean userExists(String userId) {
        try {
            return resource.users().get(userId).toRepresentation() != null;
        } catch (NotFoundException e) {
            return false;
        }
    }
}
