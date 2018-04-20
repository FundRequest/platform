package io.fundreqest.platform.tweb.request.dto;

import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.platform.github.GithubGateway;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class RequestDetailsViewMapperDecorator implements RequestDetailsViewMapper {
    @Autowired
    @Qualifier("delegate")
    private RequestDetailsViewMapper delegate;

    @Autowired
    private GithubGateway githubGateway;

    @Override
    public RequestDetailsView map(RequestDto r) {
        RequestDetailsView view = delegate.map(r);
        if (view != null) {
            IssueInformationDto issueInfo = r.getIssueInformation();
            view.setIcon("https://github.com/" + issueInfo.getOwner() + ".png");
            view.setPlatform(issueInfo.getPlatform().name());
            view.setOwner(issueInfo.getOwner());
            view.setRepo(issueInfo.getRepo());
            view.setIssueNumber(issueInfo.getNumber());
            view.setTitle(issueInfo.getTitle());
            view.setStarred(r.isLoggedInUserIsWatcher());
            view.setDescription(createHtmlDescription(issueInfo));
        }
        return view;
    }

    private String createHtmlDescription(IssueInformationDto issueInfo) {
        String body = githubGateway.getIssue(issueInfo.getOwner(), issueInfo.getRepo(), issueInfo.getNumber()).getBody();
        Parser parser = Parser.builder().build();
        Node document = parser.parse(body);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }


}
