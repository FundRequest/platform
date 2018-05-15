package io.fundrequest.platform.github;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@Builder
@ConfigurationProperties("io.fundrequest.health.github.solver")
@NoArgsConstructor
@AllArgsConstructor
public class GithubScraperHealthCheckProperties {

    private String owner;
    private String repo;
    private Map<String, String> issues;
}
