package io.fundrequest.core.user;

public final class UserDtoMother {

    public static UserDto davy() {
        UserDto userDto = new UserDto();
        userDto.setEmail("davy@fundrequest.io");
        userDto.setPhoneNumber("123456789");
        userDto.setUserId("1234567890123456789012345678901234567890");
        return userDto;
    }
}