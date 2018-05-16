package io.fundrequest.platform.github;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GithubScraperHealthCheckProperties {

    private String expectedSolver;
    private String expectedStatus;
}
