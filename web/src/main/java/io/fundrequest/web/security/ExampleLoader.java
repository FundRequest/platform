package io.fundrequest.web.security;

import org.apache.commons.lang3.StringUtils;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.representations.AccessToken;
import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class ExampleLoader implements ApplicationListener<AuthenticationSuccessEvent>, Ordered {
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        AccessToken token = ((KeycloakAuthenticationToken) SecurityContextHolder.getContext().getAuthentication()).getAccount().getKeycloakSecurityContext().getToken();
        if (StringUtils.isBlank(token.getPicture())) {
            if (token.getOtherClaims().containsKey("microsoft_id")) {
                String mId = token.getOtherClaims().get("microsoft_id").toString();
                token.setPicture("https://apis.live.net/v5.0/" + mId + "/picture?type=small");
            }
        }

    }
}