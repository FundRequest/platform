package io.fundrequest.core.config;

import io.fundrequest.core.config.social.ClientResources;
import io.fundrequest.core.config.social.SocialAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.filter.CompositeFilter;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.List;

//@Configuration
//@EnableOAuth2Client
//@EnableAuthorizationServer
//@Order(6)
public class MicroserviceSecurityConfiguration extends WebSecurityConfigurerAdapter {


//    @Autowired
//    private OAuth2ClientContext oauth2ClientContext;
//
//    @Autowired
//    private SocialAuthenticationSuccessHandler socialAuthenticationSuccessHandler;
//
//    @Bean
//    @ConditionalOnMissingBean(RequestContextListener.class)
//    public RequestContextListener requestContextListener() {
//        return new RequestContextListener();
//    }
//
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring()
//                .antMatchers(HttpMethod.OPTIONS, "/**")
//                .antMatchers("/assets/**")
//                .antMatchers("/h2-console/**");
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.antMatcher("/**").authorizeRequests()
//                .antMatchers("/", "/login**", "/webjars/**", "/management/**", "/swagger-resources/configuration/ui").permitAll()
//                .anyRequest().authenticated()
//
//                .and()
//                .exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
//
//                .and()
//                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/").permitAll()
//                .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//
//                .and()
//                .addFilterBefore(combineFilters(githubRestTemplate(github())), BasicAuthenticationFilter.class);
//    }
//
//    @Bean
//    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
//        return new SecurityEvaluationContextExtension();
//    }
//
//    @Bean
//    @ConfigurationProperties("github")
//    public ClientResources github() {
//        return new ClientResources();
//    }
//
//    private Filter combineFilters(final OAuth2RestTemplate githubRestTemplate) {
//        CompositeFilter filter = new CompositeFilter();
//        List<Filter> filters = new ArrayList<>();
//        filters.add(combineFilters(github(), "/login/github", githubRestTemplate));
//        filter.setFilters(filters);
//        return filter;
//    }
//
//    @Bean
//    public FilterRegistrationBean oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(filter);
//        registration.setOrder(-100);
//        return registration;
//    }
//
//    @Bean
//    public OAuth2RestTemplate githubRestTemplate(ClientResources client) {
//        return new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
//    }
//
//    private Filter combineFilters(ClientResources client, String path, OAuth2RestTemplate redditRestTemplate) {
//        OAuth2ClientAuthenticationProcessingFilter oAuth2ClientAuthenticationFilter = new OAuth2ClientAuthenticationProcessingFilter(path);
//        oAuth2ClientAuthenticationFilter.setRestTemplate(redditRestTemplate);
//        UserInfoTokenServices tokenServices = new UserInfoTokenServices(client.getResource().getUserInfoUri(),
//                client.getClient().getClientId());
//        tokenServices.setRestTemplate(redditRestTemplate);
//        oAuth2ClientAuthenticationFilter.setTokenServices(tokenServices);
//        oAuth2ClientAuthenticationFilter.setAuthenticationSuccessHandler(socialAuthenticationSuccessHandler);
//        return oAuth2ClientAuthenticationFilter;
//    }
//
//    @Configuration
//    @EnableResourceServer
//    protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
//        @Override
//        public void configure(HttpSecurity http) throws Exception {
//            // @formatter:off
//            http.antMatcher("/me").authorizeRequests().anyRequest().authenticated();
//            // @formatter:on
//        }
//    }

}