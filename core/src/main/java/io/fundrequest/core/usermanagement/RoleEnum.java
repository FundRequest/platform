package io.fundrequest.core.usermanagement;

public enum RoleEnum {

    ADMIN_ROLE(1L, "ROLE_ADMIN"),
    USER_ROLE(2L, "ROLE_USER");


    private final Long id;
    private final String userRole;

    RoleEnum(final Long id, final String userRole) {
        this.id = id;
        this.userRole = userRole;
    }

    public Long getId() {
        return id;
    }

    public String getUserRole() {
        return userRole;
    }

    public Role toRole() {
        return new Role(id, userRole);
    }
}