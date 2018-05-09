package io.fundrequest.platform.github;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
public class GithubSolverResolver {

    public Optional<String> solveResolver(final String owner, final String repo, final String number) {
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
                              .map(this::getAuthor)
                              .findFirst();
    }

    private String getAuthor(final Element di) {
        return di.select(".discussion-item a.author").text();
    }

    private boolean isPullRequest(final Element di) {
        Elements select = di.select(".discussion-item h3[id^=ref-pullrequest-]");
        return !select.isEmpty();
    }

    private boolean isMerged(final Element di) {
        Elements select = di.select(".discussion-item span[title=State: merged");
        return !select.isEmpty();
    }
}
