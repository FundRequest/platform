package io.fundrequest.core.request.infrastructure.github.parser;

import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.parser.GithubResult;
import org.junit.Before;
import org.junit.Test;

import static io.fundrequest.core.request.infrastructure.github.parser.GithubPlatformIdParser.PLATFORM_ID_GITHUB_DELIMTER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GithubPlatformIdParserTest {

    private GithubPlatformIdParser parser;
    private GithubGateway githubGateway;

    @Before
    public void setUp() throws Exception {
        githubGateway = mock(GithubGateway.class);
        parser = new GithubPlatformIdParser(githubGateway);
    }

    @Test
    public void parseIssueInformation() throws Exception {
        GithubResult githubResult = GithubResultMother.kazuki43zooApiStub42().build();
        String owner = "kazuki43zoo";
        String repo = "api-stub";
        String number = "42";
        when(githubGateway.getIssue(owner, repo, number))
                .thenReturn(githubResult);
        String platformId = owner + PLATFORM_ID_GITHUB_DELIMTER + repo + PLATFORM_ID_GITHUB_DELIMTER + number;

        IssueInformation result = parser.parseIssue(platformId);

        assertThat(result.getNumber()).isEqualTo(number);
        assertThat(result.getOwner()).isEqualTo(owner);
        assertThat(result.getRepo()).isEqualTo(repo);
        assertThat(result.getTitle()).isEqualTo(githubResult.getTitle());
        assertThat(result.getPlatform()).isEqualTo(Platform.GITHUB);
        assertThat(result.getPlatformId()).isEqualTo(platformId);
    }
}