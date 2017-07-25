package io.fundrequest.web.security;

import com.auth0.AuthenticationController;
import com.auth0.AuthorizeUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("unused")
@Controller
public class LoginController {

    @Autowired
    private AuthenticationController controller;

    @Value(value = "${com.auth0.callback-url}")
    private String callbackUrl;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    protected String login(final HttpServletRequest req) {
        logger.debug("Performing login");
        String redirectUri = callbackUrl;
        AuthorizeUrl authorizeUrl = controller.buildAuthorizeUrl(req, redirectUri).withScope("profile openid roles user_id name email");
        return "redirect:" + authorizeUrl.build();
    }

}