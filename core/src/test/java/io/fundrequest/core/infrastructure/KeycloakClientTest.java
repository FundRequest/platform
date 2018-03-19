package io.fundrequest.core.infrastructure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.request.claim.UserClaimRequest;
import org.junit.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.FederatedIdentityRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.util.List;

public class KeycloakClientTest {

    @Test
    public void name() throws JsonProcessingException {
        Keycloak keycloak = Keycloak.getInstance(
                "https://alpha-key.fundrequest.io/auth",
                "fundrequest",
                "fr-api-client",
                "62EHRDy*!sm1tzv4*8njo!7dKDd!16&",
                "fundrequest_dev",
                "892bc9ca-bcf0-4ceb-9e2f-ad93129f5024");
        keycloak.serverInfo();
        RealmResource realm = keycloak.realm("fundrequest");
        UserRepresentation ur = realm.users().search(null, null, null, "davy.van.roy@gmail.com", 0, 1).get(0);
        List<FederatedIdentityRepresentation> fundrequest = keycloak.realm("fundrequest").users().get(ur.getId()).getFederatedIdentity();
        System.out.println(ur);

        UserClaimRequest userClaimRequest = UserClaimRequest.builder().address("").build();
        System.out.println(new ObjectMapper().writeValueAsString(userClaimRequest));
    }
}