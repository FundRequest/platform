package io.fundrequest.platform.github.scraper;

import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.parser.GithubResult;
import io.fundrequest.platform.github.scraper.model.GithubId;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Component
public class GithubSolverResolver {

    private static final List<String> CLOSING_KEYWORDS = Arrays.asList("close", "closes", "closed", "fix", "fixes", "fixed", "resolve", "resolves", "resolved");
    private static final String CLOSING_KEYWORD_ISSUE_MATCHER_REGEX = "(?:<span.+class=\"issue-keyword.*\".*>)?\\b%1$s\\b(?:</span>)?:?\\s*<a.+class=\"issue-link js-issue-link\".*>(?:%2$s/%3$s)?#%4$s";

    private final GithubGateway githubGateway;

    public GithubSolverResolver(final GithubGateway githubGateway) {
        this.githubGateway = githubGateway;
    }

    public Optional<String> resolve(final Document document, final GithubId issueGithubId) {
        return document.select(".discussion-item")
                       .stream()
                       .filter(this::isPullRequest)
                       .filter(this::isMerged)
                       .map(this::resolvePullRequestGithubId)
                       .map(this::fetchPullrequest)
                       .filter(pullRequest -> pullRequest != null && pullRequestFixesIssue(pullRequest, issueGithubId))
                       .map(pullRequest -> pullRequest.getUser().getLogin())
                       .filter(StringUtils::isNotEmpty)
                       .findFirst();
    }

    private GithubId resolvePullRequestGithubId(final Element discussionItem) {
        if (isPullRequestInSingleDiscussionItem(discussionItem)) {
            return getPullRequestGithubIdFromSingleDiscussionItem(discussionItem);
        } else {
            return getPullRequestGithubIdFromInlineDiscussionItem(discussionItem);
        }
    }

    private GithubId getPullRequestGithubIdFromSingleDiscussionItem(final Element discussionItem) {
        return GithubId.fromString(discussionItem.select(".discussion-item [id^=ref-pullrequest-] ~ .discussion-item-ref-title a").attr("href"))
                       .orElseThrow(() -> new RuntimeException("No pullrequest identifier is found"));
    }

    private GithubId getPullRequestGithubIdFromInlineDiscussionItem(final Element discussionItem) {
        return GithubId.fromString(discussionItem.select(".discussion-item [id^=ref-pullrequest-] a").attr("href"))
                       .orElseThrow(() -> new RuntimeException("No pullrequest identifier is found"));
    }

    private GithubResult fetchPullrequest(GithubId pullRequestGithubId) {
        return githubGateway.getPullrequest(pullRequestGithubId.getOwner(), pullRequestGithubId.getRepo(), pullRequestGithubId.getNumber());
    }

    private boolean pullRequestFixesIssue(final GithubResult pullRequest, final GithubId issueGithubId) {
        final String pullRequestBody = pullRequest.getBodyHtml();
        return pullRequestBody != null && CLOSING_KEYWORDS.stream()
                                                          .map(keyword -> String.format(CLOSING_KEYWORD_ISSUE_MATCHER_REGEX,
                                                                                        keyword.toLowerCase(),
                                                                                        issueGithubId.getOwner(),
                                                                                        issueGithubId.getRepo(),
                                                                                        issueGithubId.getNumber()))
                                                          .anyMatch(regex -> Pattern.compile(regex)
                                                                                    .matcher(pullRequestBody.toLowerCase())
                                                                                    .find());
    }

    private boolean isPullRequestInSingleDiscussionItem(final Element discussionItem) {
        return discussionItem.select(".discussion-item .discussion-item-rollup-ref [id^=ref-pullrequest-]").isEmpty();
    }

    private boolean isPullRequest(final Element discussionItem) {
        Elements select = discussionItem.select(".discussion-item [id^=ref-pullrequest-]");
        return !select.isEmpty();
    }

    private boolean isMerged(final Element discussionItem) {
        Elements select = discussionItem.select(".discussion-item span[title=State: merged]");
        return !select.isEmpty();
    }
}
