package io.fundrequest.platform.github;

import io.fundrequest.platform.github.parser.GithubRateLimit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class GithubRateHealthCheck implements HealthIndicator {

    private final GithubGateway githubGateway;
    private final int threshold;

    public GithubRateHealthCheck(final GithubGateway githubGateway,
                                 @Value("${io.fundrequest.health.github.api-rate-limit.threshold-percentage:20}") final int thresholdPercentage) {
        this.githubGateway = githubGateway;
        this.threshold = thresholdPercentage;
    }

    @Override
    public Health health() {
        final GithubRateLimit rateLimit = githubGateway.getRateLimit().getCore();
        if (rateLimit.getRemaining() == 0) {
            return addDetails(Health.down(), rateLimit).build();
        }
        if (rateLimit.getRemaining() > calculateThreshold(rateLimit.getLimit())) {
            return addDetails(Health.up(), rateLimit).build();
        }
        return addDetails(Health.status(new Status("THRESHOLD REACHED")), rateLimit).build();
    }

    private int calculateThreshold(final int limit) {
        return limit * threshold / 100;
    }

    private Health.Builder addDetails(final Health.Builder healthBuilder, final GithubRateLimit rateLimit) {
        return healthBuilder.withDetail("limit", rateLimit.getLimit())
                            .withDetail("remaining", rateLimit.getRemaining())
                            .withDetail("reset", toZonedDateTime(rateLimit.getReset()));
    }

    private ZonedDateTime toZonedDateTime(final long reset) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(reset), ZoneId.systemDefault());
    }
}
