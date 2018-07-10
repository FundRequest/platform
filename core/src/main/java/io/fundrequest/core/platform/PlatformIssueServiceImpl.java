package io.fundrequest.core.platform;

import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.platform.github.GithubIssueService;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PlatformIssueServiceImpl implements PlatformIssueService {

    private final GithubIssueService githubIssueService;
    private final Mappers mappers;

    public PlatformIssueServiceImpl(final GithubIssueService githubIssueService, final Mappers mappers) {
        this.githubIssueService = githubIssueService;
        this.mappers = mappers;
    }

    @Override
    public Optional<PlatformIssueDto> findBy(final Platform platform, final String platformId) {

        if (Platform.GITHUB == platform) {
            return githubIssueService.findBy(platformId)
                                     .map(githubIssue -> mappers.map(GithubIssue.class, PlatformIssueDto.class, githubIssue));
        }
        return Optional.empty();
    }
}
