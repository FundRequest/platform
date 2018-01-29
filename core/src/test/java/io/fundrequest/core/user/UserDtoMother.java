package io.fundrequest.core.user;

import io.fundrequest.core.keycloak.UserIdentity;
import io.fundrequest.core.user.dto.UserDto;

import java.util.Collections;

public final class UserDtoMother {

    public static UserDto davy() {
        UserDto userDto = new UserDto();
        userDto.setEmail("davy@fundrequest.io");
        userDto.setUserId("12347468fas738");
        userDto.setUserIdentities(Collections.singletonList(new UserIdentity("github", "davyvanroy")));
        return userDto;
    }
}