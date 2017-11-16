package io.fundrequest.core.user;

import io.fundrequest.core.user.dto.UserDto;

public final class UserDtoMother {

    public static UserDto davy() {
        UserDto userDto = new UserDto();
        userDto.setEmail("davy@fundrequest.io");
        return userDto;
    }
}