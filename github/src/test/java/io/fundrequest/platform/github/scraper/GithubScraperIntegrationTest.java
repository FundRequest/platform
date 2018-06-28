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
    public void fetch_parsesCorrectSolver_whenReferencerInIssueIsNotOwnerPullRequest() {
        final String owner = "FundRequest";
        final String repo = "contracts";
        final String number = "48";

        final GithubIssue githubIssue = scraper.fetchGithubIssue(owner, repo, number);

        assertThat(githubIssue.getNumber()).isEqualTo("48");
        assertThat(githubIssue.getSolver()).isEqualTo("pauliax");
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

    @Test
    public void fetch_issueRandomlyReferencedInPullRequestFromSameRepo() {
        final String owner = "aragon";
        final String repo = "aragonOS";
        final String number = "280";

        final GithubIssue githubIssue = scraper.fetchGithubIssue(owner, repo, number);

        assertThat(githubIssue.getNumber()).isEqualTo("280");
        assertThat(githubIssue.getSolver()).isNull();
        assertThat(githubIssue.getStatus()).isEqualTo("Open");
    }

    @Test
    public void fetch_issueRandomlyReferencedInPullRequestFromOtherRepo() {
        final String owner = "trufflesuite";
        final String repo = "truffle";
        final String number = "501";

        final GithubIssue githubIssue = scraper.fetchGithubIssue(owner, repo, number);

        assertThat(githubIssue.getNumber()).isEqualTo("501");
        assertThat(githubIssue.getSolver()).isNull();
        assertThat(githubIssue.getStatus()).isEqualTo("Open");
    }

    @Test
    public void fetch_githubClosingKeywordsAreFound() {
        final String owner = "poanetwork";
        final String repo = "poa-explorer";
        final String number = "237";

        final GithubIssue githubIssue = scraper.fetchGithubIssue(owner, repo, number);

        assertThat(githubIssue.getNumber()).isEqualTo("237");
        assertThat(githubIssue.getSolver()).isEqualTo("katibest");
        assertThat(githubIssue.getStatus()).isEqualTo("Closed");
    }

    @Test
    public void fetch_issueFixedByPullRequestFromOtherRepo() {
        final String owner = "brave";
        final String repo = "brave-browser";
        final String number = "240";

        final GithubIssue githubIssue = scraper.fetchGithubIssue(owner, repo, number);

        assertThat(githubIssue.getNumber()).isEqualTo("240");
        assertThat(githubIssue.getSolver()).isEqualTo("cezaraugusto");
        assertThat(githubIssue.getStatus()).isEqualTo("Closed");
    }
}
