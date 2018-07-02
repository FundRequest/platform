package io.fundrequest.platform.faq;

import io.fundrequest.common.infrastructure.JsoupSpringWrapper;
import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.platform.faq.model.FaqItemDto;
import io.fundrequest.platform.faq.parser.Faq;
import io.fundrequest.platform.github.GithubGateway;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FaqItemDtoMapper implements BaseMapper<Faq, FaqItemDto> {

    private final GithubGateway githubGateway;
    private final String owner;
    private final String repo;
    private final String branch;
    private final JsoupSpringWrapper jsoup;

    public FaqItemDtoMapper(final GithubGateway githubGateway,
                            @Value("${io.fundrequest.faq.owner:FundRequest}") final String owner,
                            @Value("${io.fundrequest.faq.repo:FAQ}") final String repo,
                            @Value("${io.fundrequest.faq.branch:master}") final String branch,
                            final JsoupSpringWrapper jsoup) {
        this.githubGateway = githubGateway;
        this.owner = owner;
        this.repo = repo;
        this.branch = branch;
        this.jsoup = jsoup;
    }

    @Override
    public FaqItemDto map(final Faq faq) {
        if (faq == null) {
            return null;
        }
        try {
            final Document document = jsoup.parse(githubGateway.getContentsAsHtml(owner, repo, branch, faq.getFilePath()));
            final Elements markDownBody = document.select(".markdown-body");
            if (!markDownBody.isEmpty()) {
                final String body = markDownBody.get(0).html();
                return FaqItemDto.builder()
                                 .title(faq.getTitle())
                                 .body(body)
                                 .build();
            } else {
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong during the mapping of FaqItem '" + faq.getTitle() + "'", e);
        }
    }
}
