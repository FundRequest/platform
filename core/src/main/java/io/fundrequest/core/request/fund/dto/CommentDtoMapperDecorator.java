package io.fundrequest.core.request.fund.dto;

import io.fundrequest.platform.github.parser.GithubIssueCommentsResult;
import org.apache.commons.lang3.StringUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CommentDtoMapperDecorator implements CommentDtoMapper {

    private final HtmlRenderer renderer;
    private final Parser parser;
    @Autowired
    @Qualifier("delegate")
    private CommentDtoMapper delegate;

    public CommentDtoMapperDecorator() {
        renderer = HtmlRenderer.builder().build();
        parser = Parser.builder().build();
    }

    @Override
    public CommentDto map(GithubIssueCommentsResult in) {
        CommentDto comment = delegate.map(in);
        if (comment != null) {
            comment.setUserName(in.getUser().getLogin());
            if (StringUtils.isNotBlank(in.getBody())) {
                comment.setBody(getAsHtml(in.getBody()));
            }
        }
        return comment;
    }


    private String getAsHtml(String body) {
        Node document = parser.parse(body);
        return renderer.render(document);
    }

    void setDelegate(CommentDtoMapper delegate) {
        this.delegate = delegate;
    }
}
