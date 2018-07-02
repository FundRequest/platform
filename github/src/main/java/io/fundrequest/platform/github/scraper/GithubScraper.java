package io.fundrequest.platform.github.scraper;

import io.fundrequest.common.infrastructure.JsoupSpringWrapper;
import io.fundrequest.platform.github.scraper.model.GithubId;
import io.fundrequest.platform.github.scraper.model.GithubIssue;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubScraper {

    private final JsoupSpringWrapper jsoup;
    private final GithubSolverResolver solverResolver;
    private final GithubStatusResolver statusResolver;

    public GithubScraper(final JsoupSpringWrapper jsoup, final GithubSolverResolver solverResolver, final GithubStatusResolver statusResolver) {
        this.jsoup = jsoup;
        this.solverResolver = solverResolver;
        this.statusResolver = statusResolver;
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
                          .solver(solverResolver.resolve(document, GithubId.builder().owner(owner).repo(repo).number(number).build()).orElse(null))
                          .status(statusResolver.resolve(document))
                          .build();
    }
}
