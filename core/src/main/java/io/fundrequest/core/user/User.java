package io.fundrequest.core.user;

public class User {
    private String email;
    private String name;

    protected User() {
    }

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}
