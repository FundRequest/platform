package io.fundrequest.core.request.infrastructure.github.parser;

import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.parser.GithubResult;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class GithubPlatformIdParser {
    private GithubGateway githubGateway;

    public static final String PLATFORM_ID_GITHUB_DELIMTER = "|FR|";

    public GithubPlatformIdParser(GithubGateway githubGateway) {
        this.githubGateway = githubGateway;

    }

    public IssueInformation parseIssue(String platformId) {
        IssueInformation issueInformation = IssueInformation.builder().build();
        String[] splitted = platformId.split(Pattern.quote(PLATFORM_ID_GITHUB_DELIMTER));

        issueInformation.setOwner(splitted[0]);
        issueInformation.setRepo(splitted[1]);
        issueInformation.setNumber(splitted[2]);
        GithubResult githubResult = githubGateway.getIssue(
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
