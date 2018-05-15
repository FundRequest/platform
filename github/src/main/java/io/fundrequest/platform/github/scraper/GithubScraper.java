package io.fundrequest.platform.github.scraper;

import io.fundrequest.common.infrastructure.JsoupSpringWrapper;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubScraper {

    private final JsoupSpringWrapper jsoup;
    private final GithubSolverParser solverParser;
    private final GithubStatusParser statusParser;

    public GithubScraper(final JsoupSpringWrapper jsoup, final GithubSolverParser solverParser, final GithubStatusParser statusParser) {
        this.jsoup = jsoup;
        this.solverParser = solverParser;
        this.statusParser = statusParser;
    }

    public GithubIssue fetchGithubIssue(final String owner, final String repo, final String number) {
        Document document;
        try {
            document = jsoup.connect("https://github.com/" + owner + "/" + repo + "/issues/" + number).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return GithubIssue.builder()
                          .number(number)
                          .solver(solverParser.parse(document, owner, repo))
                          .status(statusParser.parse(document))
                          .build();
    }
}
