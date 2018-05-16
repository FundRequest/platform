package io.fundrequest.platform.github;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("io.fundrequest.health.github.scraper")
public class GithubScraperHealthChecksProperties {

    private String owner;
    private String repo;
    private Map<String, GithubScraperHealthCheckProperties> issues;
}
