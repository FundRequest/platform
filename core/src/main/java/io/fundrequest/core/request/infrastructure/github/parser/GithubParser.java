package io.fundrequest.core.request.infrastructure.github.parser;

import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.RequestSource;
import io.fundrequest.core.request.infrastructure.github.GithubClient;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class GithubParser {

    private final Pattern githubPattern;
    private GithubClient githubClient;

    public GithubParser(GithubClient githubClient) {
        this.githubClient = githubClient;
        githubPattern = Pattern.compile("^https:\\/\\/github\\.com\\/(.+)\\/(.+)\\/issues\\/(\\d+)$");

    }

    public IssueInformation parseIssue(String issueLink) {
        IssueInformation issueInformation = new IssueInformation();
        issueInformation.setLink(issueLink);
        Matcher matcher = githubPattern.matcher(issueLink);
        matcher.matches();
        issueInformation.setOwner(matcher.group(1));
        issueInformation.setRepo(matcher.group(2));
        issueInformation.setNumber(matcher.group(3));
        GithubResult githubResult = githubClient.getIssue(
                issueInformation.getOwner(),
                issueInformation.getRepo(),
                issueInformation.getNumber()
        );
        issueInformation.setTitle(githubResult.getTitle());
        issueInformation.setSource(RequestSource.GITHUB);
        return issueInformation;
    }

}
