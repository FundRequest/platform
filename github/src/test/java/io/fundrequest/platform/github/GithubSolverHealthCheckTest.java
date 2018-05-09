package io.fundrequest.platform.github;

import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GithubSolverHealthCheckTest {

    private static final String OWNER = "hgfcjgvhk";
    private static final String REPO = "fyjcgkjvhb";
    private static final String NUMBER = "364";
    private static final String EXPECTED_SOLVER = "hcfgvj";

    private GithubSolverHealthCheck githubSolverHealthCheck;
    private GithubSolverResolver githubSolverResolver;

    @Before
    public void setUp() {
        githubSolverResolver = mock(GithubSolverResolver.class);
        githubSolverHealthCheck = new GithubSolverHealthCheck(githubSolverResolver, OWNER, REPO, NUMBER, EXPECTED_SOLVER);
    }

    @Test
    public void health_up() {
        when(githubSolverResolver.solveResolver(OWNER, REPO, NUMBER)).thenReturn(Optional.of(EXPECTED_SOLVER));

        final Health result = githubSolverHealthCheck.health();

        assertThat(result.getStatus()).isEqualTo(Status.UP);
        assertThat(result.getDetails().get("usedIssue")).isEqualTo("https://github.com/" + OWNER + "/" + REPO + "/issues/" + NUMBER);
        assertThat(result.getDetails().get("expectedSolver")).isEqualTo(EXPECTED_SOLVER);

    }

    @Test
    public void health_down_whenNotFound() {
        when(githubSolverResolver.solveResolver(OWNER, REPO, NUMBER)).thenReturn(Optional.empty());

        final Health result = githubSolverHealthCheck.health();

        assertThat(result.getStatus()).isEqualTo(Status.DOWN);
        assertThat(result.getDetails().get("usedIssue")).isEqualTo("https://github.com/" + OWNER + "/" + REPO + "/issues/" + NUMBER);
        assertThat(result.getDetails().get("expectedSolver")).isEqualTo(EXPECTED_SOLVER);
        assertThat(result.getDetails().get("problem")).isEqualTo("No solver found");
    }

    @Test
    public void health_down_whenSolverDoesNotMatch() {
        final String fetchedSolver = "ghfcjg";

        when(githubSolverResolver.solveResolver(OWNER, REPO, NUMBER)).thenReturn(Optional.of(fetchedSolver));

        final Health result = githubSolverHealthCheck.health();

        assertThat(result.getStatus()).isEqualTo(Status.DOWN);
        assertThat(result.getDetails().get("usedIssue")).isEqualTo("https://github.com/" + OWNER + "/" + REPO + "/issues/" + NUMBER);
        assertThat(result.getDetails().get("expectedSolver")).isEqualTo(EXPECTED_SOLVER);
        assertThat(result.getDetails().get("fetchedSolver")).isEqualTo(fetchedSolver);
        assertThat(result.getDetails().get("problem")).isEqualTo("Fetched solver does not match expected solver");
    }

    @Test
    public void health_down_whenException() {
        doThrow(new RuntimeException()).when(githubSolverResolver).solveResolver(OWNER, REPO, NUMBER);

        final Health result = githubSolverHealthCheck.health();

        assertThat(result.getStatus()).isEqualTo(Status.DOWN);
        assertThat(result.getDetails().get("usedIssue")).isEqualTo("https://github.com/" + OWNER + "/" + REPO + "/issues/" + NUMBER);
        assertThat(result.getDetails().get("expectedSolver")).isEqualTo(EXPECTED_SOLVER);
        assertThat(result.getDetails().get("problem")).isEqualTo("Exception thrown while fetching solver");
    }
}
