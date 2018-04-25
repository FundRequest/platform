package io.fundrequest.core.user;

import io.fundrequest.core.user.dto.UserDto;

public interface UserService {
    UserDto getUser(String userId);

    @Deprecated
    UserAuthentication login(UserLoginCommand loginCommand);
}
