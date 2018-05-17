package io.fundrequest.platform.github.scraper;


import io.fundrequest.platform.github.FundRequestGithub;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes =  {FundRequestGithub.class })
public class GithubScraperIntegrationTest {

    @Autowired
    private GithubScraper scraper;

    @Test
    public void fetch() {
        final String owner = "FundRequest";
        final String repo = "area51";
        final String number = "38";

        final GithubIssue githubIssue = scraper.fetchGithubIssue(owner, repo, number);

        assertThat(githubIssue.getNumber()).isEqualTo("38");
        assertThat(githubIssue.getSolver()).isEqualTo("davyvanroy");
        assertThat(githubIssue.getStatus()).isEqualTo("Closed");
    }

    @Test
    public void fetch_parsesCorrectSolver_whenIssueIsReferencedFromMultipleIssuesOrPullRequests() {
        final String owner = "FundRequest";
        final String repo = "area51";
        final String number = "105";

        final GithubIssue githubIssue = scraper.fetchGithubIssue(owner, repo, number);

        assertThat(githubIssue.getNumber()).isEqualTo("105");
        assertThat(githubIssue.getSolver()).isEqualTo("nico-ptrs");
        assertThat(githubIssue.getStatus()).isEqualTo("Closed");
    }

    @Test
    public void fetch_whenNoSolver() {
        final String owner = "FundRequest";
        final String repo = "area51";
        final String number = "2";

        final GithubIssue githubIssue = scraper.fetchGithubIssue(owner, repo, number);

        assertThat(githubIssue.getNumber()).isEqualTo("2");
        assertThat(githubIssue.getSolver()).isNull();
        assertThat(githubIssue.getStatus()).isEqualTo("Closed");
    }
}
