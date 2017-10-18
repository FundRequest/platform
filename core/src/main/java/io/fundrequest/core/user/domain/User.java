package io.fundrequest.core.user.domain;

import io.fundrequest.core.infrastructure.repository.AbstractEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Table(name = "user")
@Entity
public class User extends AbstractEntity {
    @Id
    @Column(name = "id")
    private String userId;
    @Column(name = "phone_number")
    private String phoneNumber;
    @Column(name = "email")
    private String email;

    protected User() {
    }

    public User(String userId, String phoneNumber, String email) {
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

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(userId, user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
