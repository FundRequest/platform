package io.fundrequest.restapi.config;


import io.fundrequest.core.user.UserLoginCommand;
import io.fundrequest.core.user.UserService;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.IDToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Collections;

//@see KeycloakAuthenticationProvider
public class FrKeycloakAuthenticationProvider implements AuthenticationProvider {

    private UserService userService;

    public FrKeycloakAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) authentication;
        KeycloakAuthenticationToken keycloakAuthenticationToken = new KeycloakAuthenticationToken(token.getAccount(), token.isInteractive(), Collections.emptyList());

        IDToken idToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();
        return userService.login(new UserLoginCommand(keycloakAuthenticationToken.getPrincipal().toString(), idToken.getEmail()));
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return KeycloakAuthenticationToken.class.isAssignableFrom(aClass);
    }
}