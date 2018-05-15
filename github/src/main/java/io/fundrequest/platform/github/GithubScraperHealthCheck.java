package io.fundrequest.platform.github;

import io.fundrequest.platform.github.scraper.GithubScraper;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class GithubScraperHealthCheck implements HealthIndicator {

    private static final String DOWN_PROBLEM_KEY = "problem";

    private final GithubScraper githubScraper;
    private final String owner;
    private final String repo;
    private final Map<String, String> issues;

    public GithubScraperHealthCheck(final GithubScraper githubScraper,
                                    final GithubScraperHealthCheckProperties githubScraperHealthCheckProperties) {
        this.githubScraper = githubScraper;
        this.owner = githubScraperHealthCheckProperties.getOwner();
        this.repo = githubScraperHealthCheckProperties.getRepo();
        this.issues = githubScraperHealthCheckProperties.getIssues();
    }

    @Override
    public Health health() {
        final List<Health> healths = issues.keySet()
                                           .stream()
                                           .map(number -> checkHealth(number, issues.get(number)).withDetail("checkedURL", mapToGithubURL(number)).build())
                                           .collect(Collectors.toList());

        return Health.status(calculateOverallStatus(healths)).withDetail("healths", healths).build();
    }

    private Status calculateOverallStatus(final Collection<Health> healths) {
        final long downAmount = healths.stream()
                                       .map(Health::getStatus)
                                       .filter(Status.DOWN::equals)
                                       .count();
        return downAmount > 0 ? Status.DOWN : Status.UP;
    }

    private Health.Builder checkHealth(final String number, final String expectedSolver) {
        try {

            final Optional<String> solverOptional = Optional.ofNullable(githubScraper.fetchGithubIssue(owner, repo, number).getSolver());
            if (solverOptional.isPresent()) {
                if (expectedSolver.equals(solverOptional.get())) {
                    return Health.up();
                } else {
                    return Health.down()
                                 .withDetail("expectedSolver", expectedSolver)
                                 .withDetail("fetchedSolver", solverOptional.get())
                                 .withDetail(DOWN_PROBLEM_KEY, "Fetched solver does not match expected solver");
                }
            } else {
                return Health.down().withDetail(DOWN_PROBLEM_KEY, "No solver found");
            }
        } catch (Exception e) {
            return Health.down().withDetail(DOWN_PROBLEM_KEY, "Exception thrown while fetching solver");
        }
    }

    private String mapToGithubURL(final String number) {
        return "https://github.com/" + owner + "/" + repo + "/issues/" + number;
    }
}
