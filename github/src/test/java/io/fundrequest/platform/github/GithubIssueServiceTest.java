package io.fundrequest.platform.github;

import io.fundrequest.platform.github.scraper.GithubScraper;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GithubIssueServiceTest {

    private GithubIssueService githubIssueService;
    private GithubScraper githubScraper;

    @BeforeEach
    void setUp() {
        githubScraper = mock(GithubScraper.class);
        githubIssueService = new GithubIssueService(githubScraper);
    }

    @Test
    void findBy() {
        final String owner = "sfs";
        final String repo = "fafsa";
        final String number = "43";
        final GithubIssue githubIssue = GithubIssue.builder().build();

        when(githubScraper.fetchGithubIssue(owner, repo, number)).thenReturn(githubIssue);

        final Optional<GithubIssue> result = githubIssueService.findBy(owner + "|FR|" + repo + "|FR|" + number);

        assertThat(result).isPresent().containsSame(githubIssue);
    }

    @Test
    void findBy_invalidPlatformId() {
        final String owner = "sfs";
        final String repo = "fafsa";

        final Optional<GithubIssue> result = githubIssueService.findBy(owner + "|FR|" + repo + "|FR|");

        assertThat(result).isEmpty();
    }

    @Test
    void findBy_noIssueFound() {
        final String owner = "sfs";
        final String repo = "fafsa";
        final String number = "43";

        when(githubScraper.fetchGithubIssue(owner, repo, number)).thenReturn(null);

        final Optional<GithubIssue> result = githubIssueService.findBy(owner + "|FR|" + repo + "|FR|" + number);

        assertThat(result).isEmpty();
    }
}
