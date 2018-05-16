package io.fundrequest.platform.github;

import io.fundrequest.common.infrastructure.IgnoreDuringComponentScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableConfigurationProperties(GithubScraperHealthChecksProperties.class)
@EnableFeignClients("io.fundrequest.platform.github")
@ComponentScan(
        basePackageClasses = {FundRequestGithub.class, },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class),
                @ComponentScan.Filter(IgnoreDuringComponentScan.class)})
public class FundRequestGithub {
}
