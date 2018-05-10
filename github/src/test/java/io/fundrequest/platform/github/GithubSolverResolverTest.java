package io.fundrequest.platform.github;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class GithubSolverResolverTest {

    private GithubSolverResolver githubSolverResolver;

    @Before
    public void setUp() {
        githubSolverResolver = new GithubSolverResolver();
    }

    @Test
    public void resolvesCorrectSolver() {
        final String owner = "FundRequest";
        final String repo = "area51";
        final String number = "38";

        String solver = githubSolverResolver.solveResolver(owner, repo, number).get();

        assertThat(solver).isEqualTo("davyvanroy");
    }
}
