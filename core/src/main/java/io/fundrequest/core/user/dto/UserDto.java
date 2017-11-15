package io.fundrequest.core.user.dto;

public class UserDto {
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return "https://api.adorable.io/avatars/75/" + getEmail() + ".png";
    }
}
