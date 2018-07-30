package io.fundrequest.core.request.infrastructure.github.parser;

import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.Platform;
import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.parser.GithubResult;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GithubPlatformIdParser {
    public static final String PLATFORM_ID_GITHUB_DELIMTER = "|FR|";
    private static final Pattern PLATFORM_ID_PATTERN = Pattern.compile("(?<owner>.+)\\|FR\\|(?<repo>.+)\\|FR\\|(?<issueNumber>.+)");
    private static final String OWNER_GROUP_NAME = "owner";
    private static final String REPO_GROUP_NAME = "repo";
    private static final String ISSUE_NUMBER_GROUP_NAME = "issueNumber";
    private GithubGateway githubGateway;

    public GithubPlatformIdParser(GithubGateway githubGateway) {
        this.githubGateway = githubGateway;

    }

    public static String extractOwner(final String platformId) {
        return extract(platformId, OWNER_GROUP_NAME);
    }

    public static String extractRepo(String platformId) {
        return extract(platformId, REPO_GROUP_NAME);
    }

    public static String extractIssueNumber(final String platformId) {
        return extract(platformId, ISSUE_NUMBER_GROUP_NAME);
    }

    private static String extract(final String platformId, final String groupName) {
        final Matcher matcher = PLATFORM_ID_PATTERN.matcher(platformId);
        if (matcher.matches()) {
            return matcher.group(groupName);
        }
        throw new IllegalArgumentException("platformId '" + platformId + "' has an invalid format, <" + groupName + "> could not be extracted");
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
