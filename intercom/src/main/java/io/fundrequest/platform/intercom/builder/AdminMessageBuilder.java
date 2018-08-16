package io.fundrequest.platform.intercom.builder;

import io.intercom.api.Admin;
import io.intercom.api.AdminMessage;
import io.intercom.api.User;

public final class AdminMessageBuilder {
    private String messageType;
    private String subject="";    // Set default to blank string so null pointer exception won't thrown if messageType = inapp and subject not set
    private String body;
    private String template="plain";  // Set default to plain so null pointer exception won't thrown if messageType = inapp and template not set
    private long createdAt;
    private Admin admin;
    private User user;

    private AdminMessageBuilder() {
    }

    public static AdminMessageBuilder newInstanceWith() {
        return new AdminMessageBuilder();
    }

    public AdminMessageBuilder messageType(String messageType) {
        this.messageType = messageType;
        return this;
    }

    public AdminMessageBuilder subject(String subject) {
        this.subject = subject;
        return this;
    }

    public AdminMessageBuilder body(String body) {
        this.body = body;
        return this;
    }

    public AdminMessageBuilder template(String template) {
        this.template = template;
        return this;
    }

    public AdminMessageBuilder createdAt(long createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public AdminMessageBuilder admin(Admin admin) {
        this.admin = admin;
        return this;
    }

    public AdminMessageBuilder user(User user) {
        this.user = user;
        return this;
    }

    public AdminMessageBuilder but() {
        return newInstanceWith().messageType(messageType).subject(subject).body(body).template(template).createdAt(createdAt).admin(admin).user(user);
    }

    public AdminMessage build() {
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setMessageType(messageType);
        adminMessage.setSubject(subject);
        adminMessage.setBody(body);
        adminMessage.setTemplate(template);
        adminMessage.setCreatedAt(createdAt);
        adminMessage.setAdmin(admin);
        adminMessage.setUser(user);
        return adminMessage;
    }
}
