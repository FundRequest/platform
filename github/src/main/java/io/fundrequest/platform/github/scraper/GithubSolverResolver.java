package io.fundrequest.platform.github.scraper;

import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.parser.GithubResult;
import org.apache.commons.lang.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class GithubSolverResolver {

    private final GithubGateway githubGateway;

    public GithubSolverResolver(final GithubGateway githubGateway) {
        this.githubGateway = githubGateway;
    }

    public String resolve(final Document document, final String owner, final String repo) {
        return document.select(".discussion-item")
                       .stream()
                       .filter(this::isPullRequest)
                       .filter(this::isMerged)
                       .map(this::resolvePullRequestNumber)
                       .map(pullRequestNumber -> fetchAuthorFromPullRequest(pullRequestNumber, owner, repo))
                       .filter(StringUtils::isNotEmpty)
                       .findFirst()
                       .orElse(null);
    }

    private String resolvePullRequestNumber(final Element discussionItem) {
        final String pullRequestNumber;
        if (isPullRequestInSingleDiscussionItem(discussionItem)) {
            pullRequestNumber = getPullRequestNumberFromSingleDiscussionItem(discussionItem);
        } else {
            pullRequestNumber = getPullRequestNumberFromInlineDiscussionItem(discussionItem);
        }
        return pullRequestNumber.replace("#", "");
    }

    private String getPullRequestNumberFromSingleDiscussionItem(final Element discussionItem) {
        return discussionItem.select(".discussion-item [id^=ref-pullrequest-] ~ .discussion-item-ref-title span.issue-num").text();
    }

    private String getPullRequestNumberFromInlineDiscussionItem(final Element discussionItem) {
        return discussionItem.select(".discussion-item [id^=ref-pullrequest-] span.issue-num").text();
    }

    private String fetchAuthorFromPullRequest(final String pullRequestNumber, final String owner, final String repo) {
        final GithubResult pullRequest = githubGateway.getPullrequest(owner, repo, pullRequestNumber);
        return pullRequest.getUser().getLogin();
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
