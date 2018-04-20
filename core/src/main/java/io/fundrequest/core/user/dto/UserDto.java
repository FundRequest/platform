package io.fundrequest.core.user.dto;

import io.fundrequest.platform.keycloak.UserIdentity;

import java.util.List;

public class UserDto {
    private String userId;
    private String email;
    private List<UserIdentity> userIdentities;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserIdentity> getUserIdentities() {
        return userIdentities;
    }

    public void setUserIdentities(List<UserIdentity> userIdentities) {
        this.userIdentities = userIdentities;
    }

    public String getPicture() {
        return "https://api.adorable.io/avatars/75/" + getEmail() + ".png";
    }
}
