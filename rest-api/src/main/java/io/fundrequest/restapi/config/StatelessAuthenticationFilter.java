package io.fundrequest.restapi.config;

import io.fundrequest.core.user.UserAuthentication;
import io.fundrequest.core.user.UserService;
import io.fundrequest.restapi.security.UserJsonParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class StatelessAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatelessAuthenticationFilter.class);

    private CivicAuthClient civicAuthClient;
    private UserJsonParser userJsonParser;
    private UserService userService;
    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final String BEARER = "Bearer";
    private AuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();


    public StatelessAuthenticationFilter(CivicAuthClient civicAuthClient, UserJsonParser userJsonParser, UserService userService) {
        super(new AntPathRequestMatcher("/api/**"));
        this.civicAuthClient = civicAuthClient;
        this.userJsonParser = userJsonParser;
        this.userService = userService;
        setContinueChainBeforeSuccessfulAuthentication(true);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return StringUtils.contains(request.getHeader("Authorization"), "Bearer");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (CorsUtils.isPreFlightRequest(request)) {
            response.setStatus(HttpServletResponse.SC_OK);
            return null;
        }

        String authorizationHeader = request.getHeader(AUTH_HEADER_NAME);
        if (authorizationHeader == null || !authorizationHeader.contains(BEARER)) {
            return null;
        }

        String token = authorizationHeader.substring(BEARER.length()).trim();
        try {
            UserAuthentication userAuthentication = userService.login(userJsonParser.parseUserLoginFromJson(civicAuthClient.getIssue(token)));
            SecurityContextHolder.getContext().setAuthentication(userAuthentication);
            return userAuthentication;

        } catch (Exception e) {
            LOGGER.debug(e.getMessage(), e);
            failureHandler.onAuthenticationFailure(request, response, new AccountExpiredException("Authentication failed"));
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws
            IOException, ServletException {
        LOGGER.info("Successfull authentcation for: " + authResult.getName());
    }
}
