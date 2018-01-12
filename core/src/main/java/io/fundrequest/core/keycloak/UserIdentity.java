package io.fundrequest.core.keycloak;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserIdentity that = (UserIdentity) o;
        return Objects.equals(provider, that.provider) &&
                Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {

        return Objects.hash(provider, username);
    }
}
