package io.fundrequest.platform.github;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@Builder
@ConfigurationProperties("io.fundrequest.health.github.solver")
public class GithubSolverHealthCheckProperties {

    private String owner;
    private String repo;
    private Map<String, String> issues;
}
