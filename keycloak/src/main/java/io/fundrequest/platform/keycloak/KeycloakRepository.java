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
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class KeycloakRepository {

    private RealmResource resource;
    private String keycloakUrl;
    private final ObjectMapper objectMapper;

    public KeycloakRepository(RealmResource resource, @Value("${io.fundrequest.keycloak-custom.server-url}") String keycloakUrl) {
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
        updateAttribute(resource.users().get(userId), "ether_address", etherAddress);
    }

    public void updateTelegramName(String userId, String telegramName) {
        updateAttribute(resource.users().get(userId), "telegram_name", telegramName);
    }

    public void updateHeadline(String userId, String headline) {
        updateAttribute(resource.users().get(userId), "headline", headline);
    }

    public void updateVerifiedDeveloper(String userId, Boolean isVerified) {
        updateAttribute(resource.users().get(userId), "verified_developer", "" + BooleanUtils.isTrue(isVerified));
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
        return getAttribute(getUser(userId), "ether_address");
    }

    public String getEtherAddress(UserRepresentation userRepresentation) {
        return getAttribute(userRepresentation, "ether_address");
    }

    public boolean isVerifiedDeveloper(UserRepresentation userRepresentation) {
        return "true".equalsIgnoreCase(getAttribute(userRepresentation, "verified_developer"));
    }

    public boolean isVerifiedDeveloper(final String userId) {
        return isVerifiedDeveloper(getUser(userId));
    }

    public String getAttribute(UserRepresentation userRepresentation, String property) {
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
        return getAttribute(userRepresentation, "telegram_name");
    }

    public String getPicture(UserRepresentation userRepresentation) {
        return getAttribute(userRepresentation, "picture");
    }

    public String getHeadline(UserRepresentation userRepresentation) {
        return getAttribute(userRepresentation, "headline");
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
