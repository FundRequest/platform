package io.fundrequest.core.platform;

import io.fundrequest.common.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.platform.github.GithubIssueService;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlatformIssueServiceImplTest {

    private PlatformIssueServiceImpl platformIssueService;
    private GithubIssueService githubIssueService;
    private Mappers mappers;

    @BeforeEach
    void setUp() {
        githubIssueService = mock(GithubIssueService.class);
        mappers = mock(Mappers.class);
        platformIssueService = new PlatformIssueServiceImpl(githubIssueService, mappers);
    }

    @Test
    void findBy() {
        final String platformId = "dfsfd|FR|wfgwar|FR|243";
        final GithubIssue githubIssue = GithubIssue.builder().build();
        final PlatformIssueDto platformIssueDto = PlatformIssueDto.builder().build();

        when(githubIssueService.findBy(platformId)).thenReturn(Optional.of(githubIssue));
        when(mappers.map(GithubIssue.class, PlatformIssueDto.class, githubIssue)).thenReturn(platformIssueDto);

        final Optional<PlatformIssueDto> result = platformIssueService.findBy(Platform.GITHUB, platformId);

        assertThat(result).isPresent().containsSame(platformIssueDto);
    }

    @Test
    void findBy_noGithubIssueFound() {
        final String platformId = "dfsfd|FR|wfgwar|FR|243";

        when(githubIssueService.findBy(platformId)).thenReturn(Optional.empty());

        final Optional<PlatformIssueDto> result = platformIssueService.findBy(Platform.GITHUB, platformId);

        assertThat(result).isEmpty();
    }

    @Test
    void findBy_platformNotGithub() {
        final String platformId = "dfsfd|FR|wfgwar|FR|243";
        final GithubIssue githubIssue = GithubIssue.builder().build();
        final PlatformIssueDto platformIssueDto = PlatformIssueDto.builder().build();

        when(githubIssueService.findBy(platformId)).thenReturn(Optional.of(githubIssue));
        when(mappers.map(GithubIssue.class, PlatformIssueDto.class, githubIssue)).thenReturn(platformIssueDto);

        final Optional<PlatformIssueDto> result = platformIssueService.findBy(Platform.STACK_OVERFLOW, platformId);

        assertThat(result).isEmpty();
    }
}
