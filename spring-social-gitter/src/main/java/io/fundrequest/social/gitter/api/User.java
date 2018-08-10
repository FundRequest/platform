package io.fundrequest.social.gitter.api;

import lombok.Data;

@Data
public class User {

    private String id;
    private String username;
    private String displayName;
    private String url;
    private String avatarUrlSmall;
    private String avatarUrlMedium;
}
