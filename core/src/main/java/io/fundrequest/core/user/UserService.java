package io.fundrequest.core.user;

public interface UserService {
    UserDto getUser(String userId);

    UserAuthentication login(UserLoginCommand loginCommand);
}
