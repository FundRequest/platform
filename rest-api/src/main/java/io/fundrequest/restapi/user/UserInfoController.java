package io.fundrequest.restapi.user;

import io.fundrequest.core.user.UserService;
import io.fundrequest.core.user.dto.UserDto;
import io.fundrequest.restapi.infrastructure.AbstractRestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserInfoController extends AbstractRestController {

    private UserService userService;

    public UserInfoController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(PRIVATE_PATH + "/user/info")
    public UserDto getUserInfo(Principal principal) {
        return userService.getUser(principal.getName());
    }
}
