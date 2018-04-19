package io.fundrequest.platform.profile.profile;

import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.profile.profile.dto.UserProfile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

public interface ProfileService {
    void userProviderIdentityLinked(Principal principal, Provider provider);

    UserProfile getUserProfile(Principal principal);

    UserProfile getUserProfile(HttpServletRequest request, Principal principal);

    void updateEtherAddress(Principal principal, String etherAddress);

    void updateTelegramName(Principal principal, String telegramName);

    void updateHeadline(Principal principal, String headline);
}
