package io.fundrequest.core.request.claim.github;

import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class GithubSolverResolverTest {

    private GithubSolverResolver githubSolverResolver;

    @Before
    public void setUp() throws Exception {
        githubSolverResolver = new GithubSolverResolver();
    }

    @Test
    public void resolvesCorrectSolver() throws IOException {
        RequestDto request = RequestDtoMother.freeCodeCampNoUserStories();
        request.getIssueInformation().setOwner("FundRequest");
        request.getIssueInformation().setRepo("area51");
        request.getIssueInformation().setNumber("38");

        String solver = githubSolverResolver.solveResolver(request).get();

        assertThat(solver).isEqualTo("davyvanroy");
    }
}
