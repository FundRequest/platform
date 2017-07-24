package io.fundrequest.web.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import io.fundrequest.core.user.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.*;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

public class WebUser extends User implements Authentication {
    private DecodedJWT jwt;
    private boolean invalidated;

    public WebUser() {
    }

    public WebUser(DecodedJWT jwt) {
        super(jwt.getClaim("email").asString(), jwt.getClaim("name").asString());
        this.jwt = jwt;
    }

    private boolean hasExpired() {
        return jwt.getExpiresAt().before(new Date());
    }

    private Collection<? extends GrantedAuthority> readAuthorities(DecodedJWT jwt) {
        Claim rolesClaim = jwt.getClaim("roles");
        if (rolesClaim.isNull()) {
            return Collections.emptyList();
        }
        List<GrantedAuthority> authorities = new ArrayList<>();
        String[] scopes = rolesClaim.asArray(String.class);
        for (String s : scopes) {
            SimpleGrantedAuthority a = new SimpleGrantedAuthority(s);
            if (!authorities.contains(a)) {
                authorities.add(a);
            }
        }
        return authorities;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return readAuthorities(jwt);
    }

    @Override
    public String getCredentials() {
        return jwt == null ? null : jwt.getToken();
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return jwt.getClaim("email");
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException("Create a new Authentication object to authenticate");
        }
        invalidated = true;
    }

    @Override
    public boolean isAuthenticated() {
        return jwt != null && !invalidated && !hasExpired();
    }

    public String getFamilyName() {
        return jwt == null ? null : jwt.getClaim("family_name").asString();
    }

    public String getGivenName() {
        return jwt == null ? null : jwt.getClaim("given_name").asString();
    }

    public String getGender() {
        return jwt == null ? null : jwt.getClaim("gender").asString();
    }

    public String getNickname() {
        return jwt == null ? null : jwt.getClaim("nickname").asString();
    }

    public String getName() {
        if (isNotBlank(getGivenName())) {
            return getGivenName();
        }
        if (isNotBlank(getFamilyName())) {
            return getFamilyName();
        }
        if (isNotBlank(getNickname())) {
            return getNickname();
        }
        return "gorgeous";
    }

    public String getPicture() {
        return jwt == null ? null : jwt.getClaim("picture").asString();
    }

}
