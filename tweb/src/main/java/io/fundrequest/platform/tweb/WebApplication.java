package io.fundrequest.platform.tweb;

import io.fundrequest.common.FundRequestCommon;
import io.fundrequest.common.infrastructure.IgnoreDuringComponentScan;
import io.fundrequest.core.FundRequestCore;
import io.fundrequest.notification.FundRequestNotification;
import io.fundrequest.platform.faq.FundRequestFAQ;
import io.fundrequest.platform.github.FundRequestGithub;
import io.fundrequest.platform.gitter.FundRequestGitter;
import io.fundrequest.platform.intercom.FundRequestIntercom;
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
                FundRequestCommon.class,
                FundRequestFAQ.class,
                FundRequestNotification.class,
                FundRequestIntercom.class,
                FundRequestGitter.class
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
