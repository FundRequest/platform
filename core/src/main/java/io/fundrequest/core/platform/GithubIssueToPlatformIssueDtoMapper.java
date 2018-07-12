package io.fundrequest.core.platform;

import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.springframework.stereotype.Component;

import static io.fundrequest.core.platform.PlatformIssueStatus.CLOSED;
import static io.fundrequest.core.platform.PlatformIssueStatus.OPEN;
import static io.fundrequest.core.request.domain.Platform.GITHUB;
import static io.fundrequest.core.request.infrastructure.github.parser.GithubPlatformIdParser.PLATFORM_ID_GITHUB_DELIMTER;

@Component
public class GithubIssueToPlatformIssueDtoMapper implements BaseMapper<GithubIssue, PlatformIssueDto> {

    @Override
    public PlatformIssueDto map(final GithubIssue githubIssue) {
        return PlatformIssueDto.builder()
                               .platform(GITHUB)
                               .platformId(buildPlatformId(githubIssue))
                               .status(isClosed(githubIssue) ? CLOSED : OPEN)
                               .build();
    }

    private String buildPlatformId(final GithubIssue githubIssue) {
        return githubIssue.getOwner() + PLATFORM_ID_GITHUB_DELIMTER + githubIssue.getRepo() + PLATFORM_ID_GITHUB_DELIMTER + githubIssue.getNumber();
    }

    private boolean isClosed(final GithubIssue githubIssue) {
        return "Closed".equalsIgnoreCase(githubIssue.getStatus());
    }
}
