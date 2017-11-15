package io.fundrequest.core.user;

import java.util.Objects;

public class UserLoginCommand {
    private String email;

    public UserLoginCommand(String email) {
        this.email = email;
    }


    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserLoginCommand that = (UserLoginCommand) o;
        return Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }
}
