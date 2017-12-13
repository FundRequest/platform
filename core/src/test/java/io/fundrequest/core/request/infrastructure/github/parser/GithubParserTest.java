package io.fundrequest.core.request.infrastructure.github.parser;

import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.infrastructure.github.GithubClient;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GithubParserTest {

    private GithubParser parser;
    private GithubClient githubClient;

    @Before
    public void setUp() throws Exception {
        githubClient = mock(GithubClient.class);
        parser = new GithubParser(githubClient);
    }

    @Test
    public void parseIssueInformation() throws Exception {
        GithubResult githubResult = GithubResultMother.kazuki43zooApiStub42().build();
        when(githubClient.getIssue("kazuki43zoo", "api-stub", "42"))
                .thenReturn(githubResult);

        String link = "https://github.com/kazuki43zoo/api-stub/issues/42";
        IssueInformation result = parser.parseIssue(link);
        assertThat(result.getNumber()).isEqualTo("42");
        assertThat(result.getOwner()).isEqualTo("kazuki43zoo");
        assertThat(result.getRepo()).isEqualTo("api-stub");
        assertThat(result.getLink()).isEqualTo(link);
        assertThat(result.getTitle()).isEqualTo(githubResult.getTitle());
        assertThat(result.getPlatform()).isEqualTo(Platform.GITHUB);
        assertThat(result.getPlatform().getKey()).isEqualTo(Platform.GITHUB.getKey());
        assertThat(result.getPlatformId()).isEqualTo("198379346");
    }
}