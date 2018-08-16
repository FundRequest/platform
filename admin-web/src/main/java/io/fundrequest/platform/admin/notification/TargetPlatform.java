package io.fundrequest.platform.admin.notification;

import lombok.Getter;

@Getter
public enum TargetPlatform {
    EMAIL("_email"),
    REDDIT("_reddit");

    private String postfix;

    TargetPlatform(final String postfix) {
        this.postfix = postfix;
    }
}
