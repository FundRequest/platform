package io.fundrequest.core.config;

import com.auth0.spring.security.api.Auth0SecurityConfig;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfig extends Auth0SecurityConfig {

    /**
     * Provides Auth0 API access
     */
    @Bean
    public Auth0Client auth0Client() {
        return new Auth0Client(clientId, issuer);
    }

    /**
     * Our API Configuration - for Profile CRUD operations
     * <p>
     * Here we choose not to bother using the `auth0.securedRoute` property configuration
     * and instead ensure any unlisted endpoint in our config is secured by default
     */
    @Override
    protected void authorizeRequests(final HttpSecurity http) throws Exception {
        // include some Spring Boot Actuator endpoints to check metrics
        // add others or remove as you choose, this is just a sample config to illustrate
        // most specific rules must come - order is important (see Spring Security docs)
        http.authorizeRequests()
                .antMatchers("/css/**", "/fonts/**", "/js/**", "/login").permitAll()
                .antMatchers("/requests").hasAnyAuthority("ROLE_USER")
                .antMatchers("/docs/**").permitAll()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest().authenticated();
    }

    /*
     * Only required for sample purposes..
     */
    String getAuthorityStrategy() {
        return super.authorityStrategy;
    }
}
