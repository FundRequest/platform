package io.fundrequest.web.user;

import io.fundrequest.web.security.WebUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class UserControllerAdvice {

    @ModelAttribute("user")
    public WebUser getUser() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof WebUser) {
            return (WebUser) SecurityContextHolder.getContext().getAuthentication();
        }
        return null;
    }
}
