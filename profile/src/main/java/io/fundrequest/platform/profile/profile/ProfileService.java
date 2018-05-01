package io.fundrequest.platform.profile.profile;

import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.profile.profile.dto.UserProfile;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

public interface ProfileService {
    void userProviderIdentityLinked(Principal principal, Provider provider);

    UserProfile getUserProfile(String userId);

    UserProfile getUserProfile(Principal principal);

    void updateEtherAddress(Principal principal, String etherAddress);

    void updateTelegramName(Principal principal, String telegramName);

    void updateHeadline(Principal principal, String headline);

    String createSignupLink(HttpServletRequest request, Principal principal, Provider provider);

    void logout(Principal principal);
}
