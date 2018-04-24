package io.fundrequest.platform.github;

import io.fundrequest.platform.github.parser.GithubRateLimit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class GithubHealthCheck implements HealthIndicator {

    private final GithubGateway githubGateway;
    private final int threshold;

    public GithubHealthCheck(final GithubGateway githubGateway,
                             @Value("${github.api-rate-limit-threshold:1000}") final int threshold) {
        this.githubGateway = githubGateway;
        this.threshold = threshold;
    }

    @Override
    public Health health() {
        final GithubRateLimit rateLimit = githubGateway.getRateLimit().getCore();
        if (rateLimit.getRemaining() == 0) {
            return addDetails(Health.down(), rateLimit).build();
        }
        if (rateLimit.getRemaining() > threshold) {
            return addDetails(Health.up(), rateLimit).build();
        }
        return addDetails(Health.status(new Status("THRESHOLD REACHED")), rateLimit).build();
    }

    private Health.Builder addDetails(final Health.Builder healthBuilder, final GithubRateLimit rateLimit) {
        return healthBuilder.withDetail("limit", rateLimit.getLimit())
                            .withDetail("remaining", rateLimit.getRemaining())
                            .withDetail("reset", toLocaldateTime(rateLimit.getReset()));
    }

    private LocalDateTime toLocaldateTime(final long reset) {
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(reset), ZoneId.systemDefault());
    }
}
