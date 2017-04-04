package io.fundrequest.core.config.social;

import io.fundrequest.core.usermanagement.mapper.GithubUserMapper;
import io.fundrequest.core.usermanagement.service.GithubUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@Component
public class SocialAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

//
//    @Autowired
//    private GithubUserService githubUserService;
//    @Autowired
//    private GithubUserMapper githubUserMapper;
//
//    @Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        githubUserMapper.mapToGithubUser(authentication)
//            .ifPresent(githubUserService::upsert);
//        super.onAuthenticationSuccess(request, response, authentication);
//    }
}