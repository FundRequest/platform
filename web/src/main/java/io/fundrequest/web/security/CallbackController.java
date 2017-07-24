package io.fundrequest.web.security;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.Tokens;
import com.auth0.jwt.JWT;
import io.fundrequest.core.activity.ActivityListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("unused")
@Controller
public class CallbackController {

    @Autowired
    private AuthenticationController controller;
    private final String redirectOnFail;
    private final String redirectOnSuccess;
    private static final Logger logger = LoggerFactory.getLogger(CallbackController.class);

    public CallbackController() {
        this.redirectOnFail = "/login";
        this.redirectOnSuccess = "/";
    }

    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    protected void getCallback(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        handle(req, res);
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    protected void postCallback(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        handle(req, res);
    }

    private void handle(HttpServletRequest req, HttpServletResponse res) throws IOException {
        try {
            Tokens tokens = controller.handle(req);
            WebUser webUser = new WebUser(JWT.decode(tokens.getIdToken()));
            SecurityContextHolder.getContext().setAuthentication(webUser);

            logger.info("User '{}' has logged in.", webUser.getEmail());

            SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(req, res);
            String redirectUrl = (savedRequest == null || savedRequest.getRedirectUrl() == null) ? "/" : savedRequest.getRedirectUrl();
            res.sendRedirect(redirectUrl);
        } catch (AuthenticationException | IdentityVerificationException e) {
            e.printStackTrace();
            SecurityContextHolder.clearContext();
            res.sendRedirect(redirectOnFail);
        }
    }

}
