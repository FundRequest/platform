package io.fundrequest.platform.github;

import io.fundrequest.platform.github.scraper.GithubScraper;
import io.fundrequest.platform.github.scraper.model.GithubId;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GithubIssueService {

    private final GithubScraper githubScraper;

    public GithubIssueService(final GithubScraper githubScraper) {
        this.githubScraper = githubScraper;
    }

    public Optional<GithubIssue> findBy(final String platformId) {
        return GithubId.fromPlatformId(platformId)
                       .map(githubId -> githubScraper.fetchGithubIssue(githubId.getOwner(), githubId.getRepo(), githubId.getNumber()));
    }
}
