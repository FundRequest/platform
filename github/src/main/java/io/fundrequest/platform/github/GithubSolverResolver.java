package io.fundrequest.platform.github;

import io.fundrequest.platform.github.parser.GithubResult;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class GithubSolverResolver {

    private final GithubGateway githubGateway;

    public GithubSolverResolver(final GithubGateway githubGateway) {
        this.githubGateway = githubGateway;
    }

    public Optional<String> resolveSolver(final String owner, final String repo, final String number) {
        Document doc = null;
        try {
            doc = Jsoup.connect("https://github.com/" + owner + "/" + repo + "/issues/" + number).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Elements discussionItems = doc.select(".discussion-item");
        return discussionItems.stream()
                              .filter(this::isPullRequest)
                              .filter(this::isMerged)
                              .map(discussionItem -> getAuthor(discussionItem, owner, repo))
                              .findFirst();
    }

    private String getAuthor(final Element discussionItem, final String owner, final String repo) {
        final String author = extractAuthorFromDiscussionItem(discussionItem);
        return authorFound(author) ? author : fetchAuthorFromPullrequest(discussionItem, owner, repo);
    }

    private String extractAuthorFromDiscussionItem(final Element discussionItem) {
        return discussionItem.select(".discussion-item a.author").text();
    }

    private String fetchAuthorFromPullrequest(final Element discussionItem, final String owner, final String repo) {
        final String pullRequestNumber = discussionItem.select(".discussion-item [id^=ref-pullrequest-] span.issue-num").text();
        final GithubResult pullrequest = githubGateway.getPullrequest(owner, repo, pullRequestNumber.replace("#", ""));
        return pullrequest.getUser().getLogin();
    }

    private boolean authorFound(final String author) {
        return StringUtils.isNotEmpty(author);
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
