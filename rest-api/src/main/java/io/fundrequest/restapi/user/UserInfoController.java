package io.fundrequest.restapi.user;

import io.fundrequest.core.user.UserAuthentication;
import io.fundrequest.core.user.UserService;
import io.fundrequest.core.user.dto.UserDto;
import io.fundrequest.restapi.infrastructure.PrivateRestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserInfoController extends PrivateRestController {

    private UserService userService;

    public UserInfoController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user/info")
    public UserDto getUserInfo(Principal principal) {
        UserAuthentication userAuthentication = (UserAuthentication) principal;
        UserDto user = userService.getUser(userAuthentication.getName());
        return user;
    }
}
