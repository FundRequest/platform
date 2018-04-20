package io.fundrequest.platform.profile.profile.provider;

import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.profile.profile.dto.UserProfile;

import java.security.Principal;

public interface ProviderProfileService {
    Provider getProvider();

    void userLinked(UserProfile userProfile);

    void claimBounty(Principal principal);
}
