package io.fundrequest.web.user;

import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserControllerAdvice {

    @ModelAttribute("user")
    public AccessToken getUser() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof KeycloakAuthenticationToken) {
            AccessToken token = ((KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getAccount().getKeycloakSecurityContext().getToken();
            token.getOtherClaims().get("microsoft_id");
            return token;
        }
        return null;
    }
}
