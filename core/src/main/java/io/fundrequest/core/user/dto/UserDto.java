package io.fundrequest.core.user.dto;

public class UserDto {
    private String userId;
    private String phoneNumber;
    private String email;
    private String picture;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public String setPicture(String picture) {
        return this.picture = picture;
    }
}
