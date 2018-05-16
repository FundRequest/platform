package io.fundrequest.platform.github;

import io.fundrequest.platform.github.scraper.GithubScraper;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GithubScraperSolverHealthCheck extends AbstractGithubScraperHealthCheck {

    private final GithubScraper githubScraper;
    private final String owner;
    private final String repo;
    private final Map<String, GithubScraperHealthCheckProperties> issues;

    public GithubScraperSolverHealthCheck(final GithubScraper githubScraper, final GithubScraperHealthChecksProperties githubScraperHealthChecksProperties) {
        this.githubScraper = githubScraper;
        this.owner = githubScraperHealthChecksProperties.getOwner();
        this.repo = githubScraperHealthChecksProperties.getRepo();
        this.issues = githubScraperHealthChecksProperties.getIssues();
    }

    protected Health check(final GithubIssue githubIssue) {
        return checkProperty(mapToGithubURL(githubIssue.getNumber()), "solver", githubIssue.getSolver(), issues.get(githubIssue.getNumber()).getExpectedSolver());
    }

    @Override
    protected String getOwner() {
        return owner;
    }

    @Override
    protected String getRepo() {
        return repo;
    }

    @Override
    protected Map<String, GithubScraperHealthCheckProperties> getIssues() {
        return issues;
    }

    @Override
    protected GithubScraper getGithubScraper() {
        return githubScraper;
    }
}
