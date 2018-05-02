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

public class GithubHealthCheckTest {

    private GithubHealthCheck githubHealthCheck;
    private GithubGateway githubGateway;

    @Before
    public void setUp() throws Exception {
        githubGateway = mock(GithubGateway.class);
        githubHealthCheck = new GithubHealthCheck(githubGateway, 20);
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

        final Health resultaat = githubHealthCheck.health();

        assertThat(resultaat.getStatus()).isEqualTo(Status.UP);
        assertThat(resultaat.getDetails().get("limit")).isEqualTo(limit);
        assertThat(resultaat.getDetails().get("remaining")).isEqualTo(remaining);
        assertThat(resultaat.getDetails().get("reset")).isEqualTo(verwachteReset);
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

        final Health resultaat = githubHealthCheck.health();

        assertThat(resultaat.getStatus().getCode()).isEqualTo("THRESHOLD REACHED");
        assertThat(resultaat.getDetails().get("limit")).isEqualTo(limit);
        assertThat(resultaat.getDetails().get("remaining")).isEqualTo(remaining);
        assertThat(resultaat.getDetails().get("reset")).isEqualTo(verwachteReset);
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

        final Health resultaat = githubHealthCheck.health();

        assertThat(resultaat.getStatus()).isEqualTo(Status.DOWN);
        assertThat(resultaat.getDetails().get("limit")).isEqualTo(limit);
        assertThat(resultaat.getDetails().get("remaining")).isEqualTo(remaining);
        assertThat(resultaat.getDetails().get("reset")).isEqualTo(verwachteReset);
    }
}
