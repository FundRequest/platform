package io.fundrequest.social.gitter.api.impl;

import io.fundrequest.social.gitter.api.User;

public class UserMother {

    public static User user1() {
        final User user = new User();
        user.setId("53307831c3599d1de448e19a");
        user.setUsername("suprememoocow");
        user.setDisplayName("Andrew Newdigate");
        user.setUrl("/suprememoocow");
        user.setAvatarUrlSmall("https://avatars.githubusercontent.com/u/594566?");
        user.setAvatarUrlMedium("https://avatars.githubusercontent.com/u/594566?");
        return user;
    }
}
