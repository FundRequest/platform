package io.fundrequest.platform.github.scraper;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class GithubStatusResolver {

    public String resolve(final Document document) {
        return document.select("#partial-discussion-header .State").text();
    }
}
