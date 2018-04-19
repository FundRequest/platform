package io.fundrequest.core.user;

import io.fundrequest.core.user.dto.UserDto;
import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.keycloak.UserIdentity;

import java.util.Collections;

public final class UserDtoMother {

    public static UserDto davy() {
        UserDto userDto = new UserDto();
        userDto.setEmail("davy@fundrequest.io");
        userDto.setUserId("12347468fas738");
        userDto.setUserIdentities(Collections.singletonList(UserIdentity.builder()
                .provider(Provider.GITHUB)
                .userId(userDto.getUserId())
                .username("davyvanroy")
                .build()));
        return userDto;
    }
}