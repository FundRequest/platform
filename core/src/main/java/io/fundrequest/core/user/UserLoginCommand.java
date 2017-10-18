package io.fundrequest.core.user;

import java.util.Objects;

public class UserLoginCommand {
    private String userId;
    private String phoneNumber;
    private String email;

    public UserLoginCommand(String userId, String phoneNumber, String email) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLoginCommand that = (UserLoginCommand) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
