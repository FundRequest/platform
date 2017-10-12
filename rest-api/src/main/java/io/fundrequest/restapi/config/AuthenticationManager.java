package io.fundrequest.restapi.config;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service("customAuthManager")
public class AuthenticationManager implements org.springframework.security.authentication.AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) {
        return null;
    }
}
