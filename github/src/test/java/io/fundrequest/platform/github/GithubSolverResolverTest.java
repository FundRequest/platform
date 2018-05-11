package io.fundrequest.platform.github;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {FundRequestGithub.class })
public class GithubSolverResolverTest {

    @Autowired
    private GithubSolverResolver githubSolverResolver;

    @Test
    public void resolvesCorrectSolver() {
        final String owner = "FundRequest";
        final String repo = "area51";
        final String number = "38";

        String solver = githubSolverResolver.resolveSolver(owner, repo, number).get();

        assertThat(solver).isEqualTo("davyvanroy");
    }

    @Test
    public void resolvesCorrectSolver_WhenIssueIsReferencedFromMultipleIssuesOrPullRequests() {
        final String owner = "FundRequest";
        final String repo = "area51";
        final String number = "105";

        String solver = githubSolverResolver.resolveSolver(owner, repo, number).get();

        assertThat(solver).isEqualTo("nico-ptrs");
    }
}
