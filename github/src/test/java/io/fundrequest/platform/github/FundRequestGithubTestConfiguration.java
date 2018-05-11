package io.fundrequest.platform.github;

import io.fundrequest.common.infrastructure.IgnoreDuringComponentScan;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.util.HashMap;
import java.util.Map;

@SpringBootConfiguration
@EnableAutoConfiguration
@EnableFeignClients("io.fundrequest.platform.github")
@ComponentScan(
        basePackageClasses = {FundRequestGithubTestConfiguration.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class),
                @ComponentScan.Filter(IgnoreDuringComponentScan.class)})
public class FundRequestGithubTestConfiguration {

    @Bean(name = "owner")
    public String healthGithubSolverOwner() {
        return "FundRequest";
    }

    @Bean(name = "repo")
    public String healthGithubSolverRepo() {
        return "area51";
    }

    @Bean(name = "issues")
    public Map<String, String> healthGithubSolverIssues() {
        final HashMap<String, String> issues = new HashMap<>();
        issues.put("38", "davyvanroy");
        issues.put("105", "nico-ptrs");
        return issues;
    }
}
