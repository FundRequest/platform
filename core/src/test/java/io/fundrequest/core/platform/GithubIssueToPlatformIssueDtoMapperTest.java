package io.fundrequest.core.platform;

import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.fundrequest.core.platform.PlatformIssueStatus.CLOSED;
import static io.fundrequest.core.platform.PlatformIssueStatus.OPEN;
import static io.fundrequest.core.request.domain.Platform.GITHUB;
import static io.fundrequest.core.request.infrastructure.github.parser.GithubPlatformIdParser.PLATFORM_ID_GITHUB_DELIMTER;
import static org.assertj.core.api.Assertions.assertThat;

class GithubIssueToPlatformIssueDtoMapperTest {

    private GithubIssueToPlatformIssueDtoMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new GithubIssueToPlatformIssueDtoMapper();
    }

    @Test
    void map_statusOpen() {
        final String owner = "dbfv";
        final String repo = "sgs";
        final String number = "435";

        final PlatformIssueDto result = mapper.map(GithubIssue.builder().solver("svdzdv").owner(owner).repo(repo).number(number).status("Open").build());

        assertThat(result.getPlatform()).isEqualTo(GITHUB);
        assertThat(result.getPlatformId()).isEqualTo(owner + PLATFORM_ID_GITHUB_DELIMTER + repo + PLATFORM_ID_GITHUB_DELIMTER + number);
        assertThat(result.getStatus()).isEqualTo(OPEN);
    }

    @Test
    void map_statusClosed() {
        final String owner = "gsb";
        final String repo = "gukf";
        final String number = "3278";

        final PlatformIssueDto result = mapper.map(GithubIssue.builder().solver("svdzdv").owner(owner).repo(repo).number(number).status("Closed").build());

        assertThat(result.getPlatform()).isEqualTo(GITHUB);
        assertThat(result.getPlatformId()).isEqualTo(owner + PLATFORM_ID_GITHUB_DELIMTER + repo + PLATFORM_ID_GITHUB_DELIMTER + number);
        assertThat(result.getStatus()).isEqualTo(CLOSED);
    }
}
