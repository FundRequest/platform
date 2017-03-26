package io.fundrequest.core.usermanagement.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "githubusers")
public class GithubUser {

    @Id
    private Long id;
    @Column(name = "email")
    private String email;
    @Column(name = "name")
    private String name;
    @Column(name = "login")
    private String login;
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Column(name = "url")
    private String url;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "githubuser_role",
            joinColumns = {
                    @JoinColumn
                            (
                                    name = "user_id",
                                    referencedColumnName = "id"
                            )
            },
            inverseJoinColumns =
                    {
                            @JoinColumn
                                    (
                                            name = "role_id",
                                            referencedColumnName = "id"
                                    )
                    }
    )
    private Set<Role> authorities = new HashSet<>();

    public String getLogin() {
        return login;
    }

    public GithubUser setLogin(String login) {
        this.login = login;
        return this;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public GithubUser setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public GithubUser setUrl(String url) {
        this.url = url;
        return this;
    }

    public Long getId() {
        return id;
    }

    public GithubUser setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public GithubUser setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getName() {
        return name;
    }

    public GithubUser setName(String name) {
        this.name = name;
        return this;
    }

    public Set<Role> getAuthorities() {
        return authorities;
    }

    public GithubUser setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
        return this;
    }
}