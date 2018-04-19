package io.fundreqest.platform.tweb;

import io.fundrequest.core.FundRequestCore;
import io.fundrequest.db.infrastructure.IgnoreDuringComponentScan;
import io.fundrequest.platform.github.FundRequestGithub;
import io.fundrequest.platform.keycloak.FundRequestKeycloak;
import io.fundrequest.platform.profile.ProfileApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(
        basePackageClasses = {
                FundRequestKeycloak.class,
                FundRequestGithub.class,
                WebApplication.class,
                FundRequestCore.class,
                ProfileApplication.class,
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class),
                @ComponentScan.Filter(IgnoreDuringComponentScan.class)})
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }


}
