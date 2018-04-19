package io.fundrequest.restapi;

import io.fundrequest.core.FundRequestCore;
import io.fundrequest.db.infrastructure.IgnoreDuringComponentScan;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;

import java.net.InetAddress;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
        basePackageClasses = {RestApplication.class, FundRequestCore.class, KeycloakSecurityComponents.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class),
                @ComponentScan.Filter(IgnoreDuringComponentScan.class)})
public class RestApplication {

    private static final Logger logger = LoggerFactory.getLogger(RestApplication.class);


    public static void main(final String[] args) throws Exception {
        final ApplicationContext app = configureApplication(new SpringApplicationBuilder()).run(args);
        final Environment env = app.getEnvironment();
        logger.info("\n----------------------------------------------------------\n\t" +
                    "Application '{}' is running! Access URLs:\n\t" +
                    "Local: \t\thttp://localhost:{}\n\t" +
                    "External: \thttp://{}:{}\n\t" +
                    "Profile(s): \t{}\n----------------------------------------------------------",
                    env.getProperty("spring.application.name"),
                    env.getProperty("server.port"),
                    InetAddress.getLocalHost().getHostAddress(),
                    env.getProperty("server.port"),
                    env.getActiveProfiles());
    }

    private static SpringApplicationBuilder configureApplication(final SpringApplicationBuilder builder) {
        return builder.sources(RestApplication.class);
    }


}
