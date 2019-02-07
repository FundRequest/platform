package io.fundrequest.platform.profile.profile;

import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

public interface ProfileService {
    void userProviderIdentityLinked(Principal principal, Provider provider);

    void walletsManaged(Principal principal);

    UserProfile getNonLoggedInUserProfile(String userId);

    UserProfile getUserProfile(Principal principal);

    void updateEtherAddress(Principal principal, String etherAddress);

    String getArkaneAccessToken(KeycloakAuthenticationToken principal);

    void updateTelegramName(Principal principal, String telegramName);

    void updateHeadline(Principal principal, String headline);

    String createSignupLink(HttpServletRequest request, Principal principal, Provider provider, String redirectUrl);

    void logout(Principal principal);
}
