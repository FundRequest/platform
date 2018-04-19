package io.fundrequest.core.request.infrastructure.github.parser;

import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.core.request.infrastructure.github.GithubClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class GithubPlatformIdParser {
    private GithubClient githubClient;

    public static final String PLATFORM_ID_GITHUB_DELIMTER = "|FR|";

    public GithubPlatformIdParser(GithubClient githubClient) {
        this.githubClient = githubClient;

    }
    @Cacheable(value = "github_issue_info", key = "#platformId")
    public IssueInformation parseIssue(String platformId) {
        IssueInformation issueInformation = IssueInformation.builder().build();
        String[] splitted = platformId.split(Pattern.quote(PLATFORM_ID_GITHUB_DELIMTER));

        issueInformation.setOwner(splitted[0]);
        issueInformation.setRepo(splitted[1]);
        issueInformation.setNumber(splitted[2]);
        GithubResult githubResult = githubClient.getIssue(
                issueInformation.getOwner(),
                issueInformation.getRepo(),
                issueInformation.getNumber()
                                                         );
        issueInformation.setTitle(githubResult.getTitle());
        issueInformation.setPlatform(Platform.GITHUB);
        issueInformation.setPlatformId(platformId);
        return issueInformation;
    }


}
