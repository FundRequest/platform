package io.fundrequest.core.keycloak;

public class UserIdentity {
    private String provider;
    private String username;

    public UserIdentity(String provider, String username) {
        this.provider = provider;
        this.username = username;
    }

    public String getProvider() {
        return provider;
    }

    public String getUsername() {
        return username;
    }
}
