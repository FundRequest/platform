package io.fundrequest.web.user;

import io.fundrequest.web.security.WebUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @RequestMapping("/user")
    public String showRequests(@AuthenticationPrincipal WebUser webUser, Model model) {
        model.addAttribute("user", webUser);
        return "user";
    }
}
