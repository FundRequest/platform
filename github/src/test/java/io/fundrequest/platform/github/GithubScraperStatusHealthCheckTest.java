package io.fundrequest.platform.github;

import io.fundrequest.platform.github.scraper.GithubScraper;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GithubScraperStatusHealthCheckTest {

    private static final String OWNER = "hgfcjgvhk";
    private static final String REPO = "fyjcgkjvhb";
    private static final String NUMBER_1 = "364";
    private static final String NUMBER_2 = "766";
    private static final String EXPECTED_STATUS_1 = "open";
    private static final String EXPECTED_STATUS_2 = "closed";
    private static final String URL_PREFIX = "https://github.com/" + OWNER + "/" + REPO + "/issues/";
    private static final String URL_1 = URL_PREFIX + NUMBER_1;
    private static final String URL_2 = URL_PREFIX + NUMBER_2;

    private GithubScraperStatusHealthCheck githubScraperStatusHealthCheck;
    private GithubScraper githubScraper;

    @Before
    public void setUp() {
        githubScraper = mock(GithubScraper.class);
        final GithubScraperHealthChecksProperties githubScraperHealthChecksProperties = GithubScraperHealthChecksProperties.builder()
                                                                                                                           .owner(OWNER)
                                                                                                                           .repo(REPO)
                                                                                                                           .issues(initChecksMap())
                                                                                                                           .build();
        githubScraperStatusHealthCheck = new GithubScraperStatusHealthCheck(githubScraper, githubScraperHealthChecksProperties);
    }

    private Map<String, GithubScraperHealthCheckProperties> initChecksMap() {
        final Map<String, GithubScraperHealthCheckProperties> checksMap = new HashMap<>();
        checksMap.put(NUMBER_1, GithubScraperHealthCheckProperties.builder()
                                                                  .expectedSolver("hdfgjg")
                                                                  .expectedStatus(EXPECTED_STATUS_1)
                                                                  .build());
        checksMap.put(NUMBER_2, GithubScraperHealthCheckProperties.builder()
                                                                  .expectedSolver("jglkl")
                                                                  .expectedStatus(EXPECTED_STATUS_2)
                                                                  .build());
        return checksMap;
    }

    @Test
    public void health_up() {
        final List<Health> expectedHealths = Arrays.asList(Health.up().withDetail("checkedURL", URL_1).build(),
                                                           Health.up().withDetail("checkedURL", URL_2).build());

        when(githubScraper.fetchGithubIssue(OWNER, REPO, NUMBER_1)).thenReturn(GithubIssue.builder().number(NUMBER_1).status(EXPECTED_STATUS_1).build());
        when(githubScraper.fetchGithubIssue(OWNER, REPO, NUMBER_2)).thenReturn(GithubIssue.builder().number(NUMBER_2).status(EXPECTED_STATUS_2).build());

        final Health result = githubScraperStatusHealthCheck.health();

        assertThat(result.getStatus()).isEqualTo(Status.UP);
        assertThat(result.getDetails().get("healths")).isEqualTo(expectedHealths);

    }

    @Test
    public void health_down_when1NotFound() {
        final List<Health> expectedHealths = Arrays.asList(Health.down().withDetail("problem", "No status found").withDetail("checkedURL", URL_1).build(),
                                                           Health.up().withDetail("checkedURL", URL_2).build());

        when(githubScraper.fetchGithubIssue(OWNER, REPO, NUMBER_1)).thenReturn(GithubIssue.builder().number(NUMBER_1).build());
        when(githubScraper.fetchGithubIssue(OWNER, REPO, NUMBER_2)).thenReturn(GithubIssue.builder().number(NUMBER_2).status(EXPECTED_STATUS_2).build());

        final Health result = githubScraperStatusHealthCheck.health();

        assertThat(result.getStatus()).isEqualTo(Status.DOWN);
        assertThat(result.getDetails().get("healths")).isEqualTo(expectedHealths);
    }

    @Test
    public void health_down_when1StatusDoesNotMatch() {
        final String fetchedStatus = "fhgdjg";
        final List<Health> expectedHealths = Arrays.asList(Health.up().withDetail("checkedURL", URL_1).build(),
                                                           Health.down()
                                                                 .withDetail("expectedStatus", EXPECTED_STATUS_2)
                                                                 .withDetail("fetchedStatus", fetchedStatus)
                                                                 .withDetail("problem", "Fetched status does not match expected status")
                                                                 .withDetail("checkedURL", URL_2)
                                                                 .build());

        when(githubScraper.fetchGithubIssue(OWNER, REPO, NUMBER_1)).thenReturn(GithubIssue.builder().number(NUMBER_1).status(EXPECTED_STATUS_1).build());
        when(githubScraper.fetchGithubIssue(OWNER, REPO, NUMBER_2)).thenReturn(GithubIssue.builder().number(NUMBER_2).status(fetchedStatus).build());

        final Health result = githubScraperStatusHealthCheck.health();

        assertThat(result.getStatus()).isEqualTo(Status.DOWN);
        assertThat(result.getDetails().get("healths")).isEqualTo(expectedHealths);
    }

    @Test
    public void health_down_when1ThrowsException() {
        when(githubScraper.fetchGithubIssue(OWNER, REPO, NUMBER_1)).thenReturn(GithubIssue.builder().number(NUMBER_1).status(EXPECTED_STATUS_1).build());
        doThrow(new RuntimeException()).when(githubScraper).fetchGithubIssue(OWNER, REPO, NUMBER_2);

        final Health result = githubScraperStatusHealthCheck.health();

        assertThat(result.getStatus()).isEqualTo(Status.DOWN);
    }
}
