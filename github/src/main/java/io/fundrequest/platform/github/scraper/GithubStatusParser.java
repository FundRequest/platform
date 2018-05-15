package io.fundrequest.platform.github.scraper;

import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

@Component
public class GithubStatusParser {

    public String parse(final Document document) {
        return document.select("#partial-discussion-header .State").text();
    }
}
