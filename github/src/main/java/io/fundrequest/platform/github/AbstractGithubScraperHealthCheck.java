package io.fundrequest.platform.github;

import io.fundrequest.platform.github.scraper.GithubScraper;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractGithubScraperHealthCheck implements HealthIndicator {

    private static final String DOWN_PROBLEM_KEY = "problem";

    protected abstract String getOwner();

    protected abstract String getRepo();

    protected abstract Map<String, GithubScraperHealthCheckProperties> getIssues();

    protected abstract GithubScraper getGithubScraper();

    protected abstract Health check(GithubIssue githubIssue);

    @Override
    public Health health() {
        try {
            final List<Health> healths = getIssues().keySet()
                                                    .stream()
                                                    .map(number -> getGithubScraper().fetchGithubIssue(getOwner(), getRepo(), number))
                                                    .map(this::check)
                                                    .collect(Collectors.toList());
            return Health.status(calculateOverallStatus(healths)).withDetail("healths", healths).build();
        } catch (Exception e) {
            return Health.down().withDetail(DOWN_PROBLEM_KEY, "Exception thrown while fetching GitHub issue").build();
        }
    }

    protected Health checkProperty(final String githubURL, final String propertyName, final String fetched, final String expected) {
        final Health.Builder healthBuilder;
        if (fetched != null) {
            if (expected.equals(fetched)) {
                healthBuilder = Health.up();
            } else {
                healthBuilder = Health.down()
                                      .withDetail("expected" + StringUtils.capitalize(propertyName), expected)
                                      .withDetail("fetched" + StringUtils.capitalize(propertyName), fetched)
                                      .withDetail(DOWN_PROBLEM_KEY,
                                                  String.format("Fetched %1$s does not match expected %s", propertyName));
            }
        } else {
            healthBuilder = Health.down().withDetail(DOWN_PROBLEM_KEY, String.format("No %1$s found", propertyName));
        }
        return healthBuilder.withDetail("checkedURL", githubURL).build();
    }

    protected String mapToGithubURL(final String number) {
        return "https://github.com/" + getOwner() + "/" + getRepo() + "/issues/" + number;
    }

    protected Status calculateOverallStatus(final Collection<Health> healths) {
        final long downAmount = healths.stream()
                                       .map(Health::getStatus)
                                       .filter(Status.DOWN::equals)
                                       .count();
        return downAmount > 0 ? Status.DOWN : Status.UP;
    }
}
