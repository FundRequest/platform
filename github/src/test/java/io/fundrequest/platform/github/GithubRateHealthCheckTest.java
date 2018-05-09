package io.fundrequest.platform.github;

import io.fundrequest.platform.github.parser.GithubRateLimit;
import io.fundrequest.platform.github.parser.GithubRateLimits;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GithubRateHealthCheckTest {

    private GithubRateHealthCheck githubRateHealthCheck;
    private GithubGateway githubGateway;

    @Before
    public void setUp() throws Exception {
        githubGateway = mock(GithubGateway.class);
        githubRateHealthCheck = new GithubRateHealthCheck(githubGateway, 20);
    }

    @Test
    public void testHealth_up() {
        final int limit = 5000;
        final int remaining = 1001;
        final long reset = 1372700873;
        final ZonedDateTime verwachteReset = ZonedDateTime.ofInstant(Instant.ofEpochSecond(reset), ZoneId.systemDefault());

        when(githubGateway.getRateLimit()).thenReturn(GithubRateLimits.with()
                                                                      .core(GithubRateLimit.with()
                                                                                           .limit(limit)
                                                                                           .remaining(remaining)
                                                                                           .reset(reset)
                                                                                           .build())
                                                                      .build());

        final Health result = githubRateHealthCheck.health();

        assertThat(result.getStatus()).isEqualTo(Status.UP);
        assertThat(result.getDetails().get("limit")).isEqualTo(limit);
        assertThat(result.getDetails().get("remaining")).isEqualTo(remaining);
        assertThat(result.getDetails().get("reset")).isEqualTo(verwachteReset);
    }

    @Test
    public void testHealth_thresholdReached() {
        final int limit = 5000;
        final int remaining = 1000;
        final long reset = 1372700873;
        final ZonedDateTime verwachteReset = ZonedDateTime.ofInstant(Instant.ofEpochSecond(reset), ZoneId.systemDefault());

        when(githubGateway.getRateLimit()).thenReturn(GithubRateLimits.with()
                                                                      .core(GithubRateLimit.with()
                                                                                           .limit(limit)
                                                                                           .remaining(remaining)
                                                                                           .reset(reset)
                                                                                           .build())
                                                                      .build());

        final Health result = githubRateHealthCheck.health();

        assertThat(result.getStatus().getCode()).isEqualTo("THRESHOLD REACHED");
        assertThat(result.getDetails().get("limit")).isEqualTo(limit);
        assertThat(result.getDetails().get("remaining")).isEqualTo(remaining);
        assertThat(result.getDetails().get("reset")).isEqualTo(verwachteReset);
    }

    @Test
    public void testHealth_down() {
        final int limit = 5000;
        final int remaining = 0;
        final long reset = 1372700873;
        final ZonedDateTime verwachteReset = ZonedDateTime.ofInstant(Instant.ofEpochSecond(reset), ZoneId.systemDefault());

        when(githubGateway.getRateLimit()).thenReturn(GithubRateLimits.with()
                                                                      .core(GithubRateLimit.with()
                                                                                           .limit(limit)
                                                                                           .remaining(remaining)
                                                                                           .reset(reset)
                                                                                           .build())
                                                                      .build());

        final Health result = githubRateHealthCheck.health();

        assertThat(result.getStatus()).isEqualTo(Status.DOWN);
        assertThat(result.getDetails().get("limit")).isEqualTo(limit);
        assertThat(result.getDetails().get("remaining")).isEqualTo(remaining);
        assertThat(result.getDetails().get("reset")).isEqualTo(verwachteReset);
    }
}
