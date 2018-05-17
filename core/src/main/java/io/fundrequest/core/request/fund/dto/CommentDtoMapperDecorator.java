package io.fundrequest.core.request.fund.dto;

import io.fundrequest.platform.github.parser.GithubIssueCommentsResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class CommentDtoMapperDecorator implements CommentDtoMapper {

    @Autowired
    @Qualifier("delegate")
    private CommentDtoMapper delegate;

    @Override
    public CommentDto map(GithubIssueCommentsResult in) {
        CommentDto comment = delegate.map(in);
        if (comment != null) {
            comment.setUserName(in.getUser().getLogin());
            comment.setUserUrl(in.getUser().getUrl());
            comment.setUserAvatar(in.getUser().getAvatarUrl());
        }
        return comment;
    }

    void setDelegate(CommentDtoMapper delegate) {
        this.delegate = delegate;
    }
}
