package io.fundrequest.core.request.claim.github;

import io.fundrequest.core.request.view.RequestDto;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubSolverResolver {

    public String solveResolver(RequestDto request) throws IOException {
        Document doc = Jsoup.connect("https://github.com/" +
                request.getIssueInformation().getOwner() +
                "/" + request.getIssueInformation().getRepo() +
                "/issues/" + request.getIssueInformation().getNumber()
        ).get();
        Elements discussionItems = doc.select(".discussion-item");
        return discussionItems.stream()
                .filter(this::isPullRequest)
                .filter(this::isMerged)
                .map(this::getAuthor)
                .findFirst().orElseThrow(() -> new RuntimeException("Unable to find solver"));
    }

    private String getAuthor(Element di) {
        return di.select(".discussion-item a.author").text();
    }

    private boolean isPullRequest(Element di) {
        Elements select = di.select(".discussion-item h3[id^=ref-pullrequest-]");
        return select.size() > 0;
    }

    private boolean isMerged(Element di) {
        Elements select = di.select(".discussion-item span[title=State: merged");
        return select.size() > 0;
    }
}
