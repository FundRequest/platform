package io.fundrequest.platform.intercom.builder;

import com.google.common.collect.Maps;
import io.intercom.api.Avatar;
import io.intercom.api.CompanyCollection;
import io.intercom.api.CustomAttribute;
import io.intercom.api.User;

import java.util.Map;

public final class UserBuilder {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String userId;
    private Avatar avatar;
    private long remoteCreatedAt;
    private boolean unsubscribedFromEmails = false;
    private long lastRequestAt;
    private long signedUpAt;
    private String lastSeenIp;
    private Map<String, CustomAttribute> customAttributes = Maps.newHashMap();
    private String userAgentData;
    private CompanyCollection companyCollection = new CompanyCollection();
    private boolean updateLastRequestAt = false;
    private boolean newSession = false;

    private UserBuilder() {
    }

    public static UserBuilder newInstanceWith() {
        return new UserBuilder();
    }

    public UserBuilder id(String id) {
        this.id = id;
        return this;
    }

    public UserBuilder name(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder phone(String phone) {
        this.phone = phone;
        return this;
    }

    public UserBuilder userId(String userId) {
        this.userId = userId;
        return this;
    }

    public UserBuilder avatar(Avatar avatar) {
        this.avatar = avatar;
        return this;
    }

    public UserBuilder remoteCreatedAt(long remoteCreatedAt) {
        this.remoteCreatedAt = remoteCreatedAt;
        return this;
    }

    public UserBuilder unsubscribedFromEmails(Boolean unsubscribedFromEmails) {
        this.unsubscribedFromEmails = unsubscribedFromEmails;
        return this;
    }

    public UserBuilder lastRequestAt(long lastRequestAt) {
        this.lastRequestAt = lastRequestAt;
        return this;
    }

    public UserBuilder signedUpAt(long signedUpAt) {
        this.signedUpAt = signedUpAt;
        return this;
    }

    public UserBuilder lastSeenIp(String lastSeenIp) {
        this.lastSeenIp = lastSeenIp;
        return this;
    }

    public UserBuilder customAttributes(Map<String, CustomAttribute> customAttributes) {
        this.customAttributes = customAttributes;
        return this;
    }

    public UserBuilder userAgentData(String userAgentData) {
        this.userAgentData = userAgentData;
        return this;
    }

    public UserBuilder companyCollection(CompanyCollection companyCollection) {
        this.companyCollection = companyCollection;
        return this;
    }

    public UserBuilder updateLastRequestAt(Boolean updateLastRequestAt) {
        this.updateLastRequestAt = updateLastRequestAt;
        return this;
    }

    public UserBuilder newSession(Boolean newSession) {
        this.newSession = newSession;
        return this;
    }

    public UserBuilder but() {
        return newInstanceWith().id(id)
                                .name(name)
                                .email(email)
                                .phone(phone)
                                .userId(userId)
                                .avatar(avatar)
                                .remoteCreatedAt(remoteCreatedAt)
                                .unsubscribedFromEmails(unsubscribedFromEmails)
                                .lastRequestAt(lastRequestAt)
                                .signedUpAt(signedUpAt)
                                .lastSeenIp(lastSeenIp)
                                .customAttributes(customAttributes)
                                .userAgentData(userAgentData)
                                .companyCollection(companyCollection)
                                .updateLastRequestAt(updateLastRequestAt)
                                .newSession(newSession);
    }

    public User build() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        user.setPhone(phone);
        user.setUserId(userId);
        user.setAvatar(avatar);
        user.setRemoteCreatedAt(remoteCreatedAt);
        user.setUnsubscribedFromEmails(unsubscribedFromEmails);
        user.setLastRequestAt(lastRequestAt);
        user.setSignedUpAt(signedUpAt);
        user.setLastSeenIp(lastSeenIp);
        user.setCustomAttributes(customAttributes);
        user.setUserAgentData(userAgentData);
        user.setCompanyCollection(companyCollection);
        user.setUpdateLastRequestAt(updateLastRequestAt);
        user.setNewSession(newSession);
        return user;
    }
}
