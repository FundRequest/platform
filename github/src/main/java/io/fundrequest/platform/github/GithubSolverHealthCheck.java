package io.fundrequest.platform.github;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GithubSolverHealthCheck implements HealthIndicator {

    private static final String DOWN_PROBLEM_KEY = "problem";

    private final GithubSolverResolver githubSolverResolver;
    private final String owner;
    private final String repo;
    private final String number;
    private final String solver;

    private final Health.Builder healthUp;
    private final Health.Builder healthDownBuilder;

    public GithubSolverHealthCheck(final GithubSolverResolver githubSolverResolver,
                                   @Value("${io.fundrequest.health.github.solver.owner:FundRequest}") final String owner,
                                   @Value("${io.fundrequest.health.github.solver.repo:area51}") final String repo,
                                   @Value("${io.fundrequest.health.github.solver.number:38}") final String number,
                                   @Value("${io.fundrequest.health.github.solver.expected-solver:davyvanroy}") final String expectedSolver) {
        this.githubSolverResolver = githubSolverResolver;
        this.owner = owner;
        this.repo = repo;
        this.number = number;
        this.solver = expectedSolver;

        final String usedIssue = "https://github.com/" + owner + "/" + repo + "/issues/" + number;
        this.healthUp = Health.up()
                              .withDetail("usedIssue", usedIssue)
                              .withDetail("expectedSolver", expectedSolver);

        this.healthDownBuilder = Health.down()
                                       .withDetail("usedIssue", usedIssue)
                                       .withDetail("expectedSolver", expectedSolver);
    }

    @Override
    public Health health() {
        try {
            final Optional<String> solverOptional = githubSolverResolver.solveResolver(owner, repo, number);
            if (solverOptional.isPresent()) {
                if (solver.equals(solverOptional.get())) {
                    return healthUp.build();
                } else {
                    return healthDownBuilder.withDetail("fetchedSolver", solverOptional.get())
                                            .withDetail(DOWN_PROBLEM_KEY, "Fetched solver does not match expected solver")
                                            .build();
                }
            } else {
                return healthDownBuilder.withDetail(DOWN_PROBLEM_KEY, "No solver found").build();
            }
        } catch (Exception e) {
            return healthDownBuilder.withDetail(DOWN_PROBLEM_KEY, "Exception thrown while fetching solver").build();
        }
    }

}
