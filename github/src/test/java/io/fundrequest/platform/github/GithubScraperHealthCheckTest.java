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

public class GithubScraperHealthCheckTest {

    private static final String OWNER = "hgfcjgvhk";
    private static final String REPO = "fyjcgkjvhb";
    private static final String NUMBER_1 = "364";
    private static final String NUMBER_2 = "766";
    private static final String EXPECTED_SOLVER_1 = "hcfgvj";
    private static final String EXPECTED_SOLVER_2 = "gcfhsa";
    private static final String URL_PREFIX = "https://github.com/" + OWNER + "/" + REPO + "/issues/";
    private static final String URL_1 = URL_PREFIX + NUMBER_1;
    private static final String URL_2 = URL_PREFIX + NUMBER_2;

    private GithubScraperHealthCheck githubScraperHealthCheck;
    private GithubScraper githubScraper;

    @Before
    public void setUp() {
        githubScraper = mock(GithubScraper.class);
        final GithubScraperHealthCheckProperties githubScraperHealthCheckProperties = GithubScraperHealthCheckProperties.builder()
                                                                                                                        .owner(OWNER)
                                                                                                                        .repo(REPO)
                                                                                                                        .issues(initChecksMap())
                                                                                                                        .build();
        githubScraperHealthCheck = new GithubScraperHealthCheck(githubScraper, githubScraperHealthCheckProperties);
    }

    private Map<String, String> initChecksMap() {
        final Map<String, String> checksMap = new HashMap<>();
        checksMap.put(NUMBER_1, EXPECTED_SOLVER_1);
        checksMap.put(NUMBER_2, EXPECTED_SOLVER_2);
        return checksMap;
    }

    @Test
    public void health_up() {
        final List<Health> expectedHealths = Arrays.asList(Health.up().withDetail("checkedURL", URL_1).build(),
                                                           Health.up().withDetail("checkedURL", URL_2).build());

        when(githubScraper.fetchGithubIssue(OWNER, REPO, NUMBER_1)).thenReturn(GithubIssue.builder().solver(EXPECTED_SOLVER_1).build());
        when(githubScraper.fetchGithubIssue(OWNER, REPO, NUMBER_2)).thenReturn(GithubIssue.builder().solver(EXPECTED_SOLVER_2).build());

        final Health result = githubScraperHealthCheck.health();



        assertThat(result.getStatus()).isEqualTo(Status.UP);
        assertThat(result.getDetails().get("healths")).isEqualTo(expectedHealths);

    }

    @Test
    public void health_down_when1NotFound() {
        final List<Health> expectedHealths = Arrays.asList(Health.down().withDetail("problem", "No solver found").withDetail("checkedURL", URL_1).build(),
                                                           Health.up().withDetail("checkedURL", URL_2).build());

        when(githubScraper.fetchGithubIssue(OWNER, REPO, NUMBER_1)).thenReturn(GithubIssue.builder().build());
        when(githubScraper.fetchGithubIssue(OWNER, REPO, NUMBER_2)).thenReturn(GithubIssue.builder().solver(EXPECTED_SOLVER_2).build());

        final Health result = githubScraperHealthCheck.health();

        assertThat(result.getStatus()).isEqualTo(Status.DOWN);
        assertThat(result.getDetails().get("healths")).isEqualTo(expectedHealths);
    }

    @Test
    public void health_down_when1SolverDoesNotMatch() {
        final String fetchedSolver = "fhgdjg";
        final List<Health> expectedHealths = Arrays.asList(Health.up().withDetail("checkedURL", URL_1).build(),
                                                           Health.down()
                                                                 .withDetail("expectedSolver", EXPECTED_SOLVER_2)
                                                                 .withDetail("fetchedSolver", fetchedSolver)
                                                                 .withDetail("problem", "Fetched solver does not match expected solver")
                                                                 .withDetail("checkedURL", URL_2)
                                                                 .build());

        when(githubScraper.fetchGithubIssue(OWNER, REPO, NUMBER_1)).thenReturn(GithubIssue.builder().solver(EXPECTED_SOLVER_1).build());
        when(githubScraper.fetchGithubIssue(OWNER, REPO, NUMBER_2)).thenReturn(GithubIssue.builder().solver(fetchedSolver).build());

        final Health result = githubScraperHealthCheck.health();

        assertThat(result.getStatus()).isEqualTo(Status.DOWN);
        assertThat(result.getDetails().get("healths")).isEqualTo(expectedHealths);
    }

    @Test
    public void health_down_when1ThrowsException() {
        final List<Health> expectedHealths = Arrays.asList(Health.up().withDetail("checkedURL", URL_1).build(),
                                                           Health.down().withDetail("problem", "Exception thrown while fetching solver").withDetail("checkedURL", URL_2).build());

        when(githubScraper.fetchGithubIssue(OWNER, REPO, NUMBER_1)).thenReturn(GithubIssue.builder().solver(EXPECTED_SOLVER_1).build());
        doThrow(new RuntimeException()).when(githubScraper).fetchGithubIssue(OWNER, REPO, NUMBER_2);

        final Health result = githubScraperHealthCheck.health();

        assertThat(result.getStatus()).isEqualTo(Status.DOWN);
        assertThat(result.getDetails().get("healths")).isEqualTo(expectedHealths);
    }
}
