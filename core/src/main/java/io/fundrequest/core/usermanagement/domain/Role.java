package io.fundrequest.core.usermanagement.domain;


import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "role")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "authority")
    private String authority;

    public Role(final Long id, final String authority) {
        this.id = id;
        this.authority = authority;
    }

    public Role() {
    }

    public Long getId() {
        return id;
    }

    public Role setId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public String getAuthority() {
        return authority;
    }

    public Role setAuthority(String authority) {
        this.authority = authority;
        return this;
    }
}
