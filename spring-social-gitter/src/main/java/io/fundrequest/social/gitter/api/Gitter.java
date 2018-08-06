package io.fundrequest.social.gitter.api;

import org.springframework.social.ApiBinding;

public interface Gitter extends ApiBinding {

    MessageResource messageResource();

    RoomResource roomResource();

    UserResource userResource();
}
