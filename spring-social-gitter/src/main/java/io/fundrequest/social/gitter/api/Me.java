package io.fundrequest.social.gitter.api;

import lombok.Data;

@Data
public class Me {
    private String id;
    private String username;
    private String displayName;
    private String url;
    private String avatarUrl;
}
