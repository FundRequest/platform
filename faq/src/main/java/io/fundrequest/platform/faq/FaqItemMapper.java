package io.fundrequest.platform.faq;

import io.fundrequest.common.infrastructure.JsoupSpringWrapper;
import io.fundrequest.core.infrastructure.mapping.BaseMapper;
import io.fundrequest.platform.faq.model.FaqItemDto;
import io.fundrequest.platform.faq.parser.Faq;
import io.fundrequest.platform.github.GithubGateway;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FaqItemMapper implements BaseMapper<Faq, FaqItemDto> {

    private final GithubGateway githubGateway;
    private final String owner;
    private final String repo;
    private final JsoupSpringWrapper jsoup;

    public FaqItemMapper(final GithubGateway githubGateway,
                         @Value("${io.fundrequest.faq.owner:FundRequest}") final String owner,
                         @Value("${io.fundrequest.faq.repo:FAQ}") final String repo,
                         final JsoupSpringWrapper jsoup) {

        this.githubGateway = githubGateway;
        this.owner = owner;
        this.repo = repo;
        this.jsoup = jsoup;
    }

    @Override
    public FaqItemDto map(final Faq faq) {
        if (faq == null) {
            return null;
        }
        try {
            final Document document = jsoup.parse(githubGateway.getContentsAsHtml(owner, repo, faq.getFilePath()));
            final String body = document.select(".markdown-body").get(0).html();

            return FaqItemDto.builder()
                             .title(faq.getTitle())
                             .body(body)
                             .build();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong during the mapping of FaqItem '" + faq.getTitle() + "'", e);
        }
    }
}
