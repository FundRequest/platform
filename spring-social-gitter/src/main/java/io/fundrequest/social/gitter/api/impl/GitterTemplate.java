package io.fundrequest.social.gitter.api.impl;

import io.fundrequest.social.gitter.api.Gitter;
import io.fundrequest.social.gitter.api.MessageResource;
import io.fundrequest.social.gitter.api.RoomResource;
import io.fundrequest.social.gitter.api.UserResource;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;

public class GitterTemplate extends AbstractOAuth2ApiBinding implements Gitter {

    private final UserResourceTemplate userResourceTemplate;
    private final RoomResourceTemplate roomResourceTemplate;
    private final MessageResourceTemplate messageResourceTemplate;

    public GitterTemplate(final String accessToken) {
        super(accessToken);
        userResourceTemplate = new UserResourceTemplate(getRestTemplate());
        roomResourceTemplate = new RoomResourceTemplate(getRestTemplate());
        messageResourceTemplate = new MessageResourceTemplate(getRestTemplate());
    }

    @Override
    public UserResource userResource() {
        return userResourceTemplate;
    }

    @Override
    public RoomResource roomResource() {
        return roomResourceTemplate;
    }

    @Override
    public MessageResource messageResource() {
        return messageResourceTemplate;
    }
}
