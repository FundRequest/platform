package io.fundrequest.core.identity;

import org.keycloak.representations.idm.UserRepresentation;

public class IdentityAPIClientDummy implements IdentityAPIClient {

    @Override
    public UserRepresentation findByIdentityProviderAndFederatedUsername(final String identityProvider, final String federatedUsername) {
        final UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId("rtykjgh");
        userRepresentation.setEmail("mock@fundrequest.io");
        return userRepresentation;
    }
}
