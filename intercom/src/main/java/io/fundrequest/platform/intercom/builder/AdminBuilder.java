package io.fundrequest.platform.intercom.builder;

import io.intercom.api.Admin;

public final class AdminBuilder {
    private String id;

    private AdminBuilder() {
    }

    public static AdminBuilder newInstanceWith() {
        return new AdminBuilder();
    }

    public AdminBuilder id(String id) {
        this.id = id;
        return this;
    }

    public AdminBuilder but() {
        return newInstanceWith().id(id);
    }

    public Admin build() {
        Admin admin = new Admin();
        admin.setId(id);
        return admin;
    }
}
