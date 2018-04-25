package io.fundrequest.core.request.fund.dto;

import io.fundrequest.platform.github.parser.GithubIssueCommentsResult;
import io.fundrequest.platform.github.parser.GithubUser;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommentDtoMapperDecoratorTest {

    private CommentDtoMapperDecorator decorator;
    private CommentDtoMapper delegate;

    @Before
    public void setUp() throws Exception {
        decorator = new CommentDtoMapperDecorator();
        delegate = mock(CommentDtoMapper.class);
        decorator.setDelegate(delegate);
    }

    @Test
    public void maps() {
        when(delegate.map(any())).thenReturn(CommentDto.builder().build());
        CommentDto result = decorator.map(GithubIssueCommentsResult.builder().user(GithubUser.builder().login("davyvanroy").build()).body("# body").build());

        assertThat(result.getUserName()).isEqualTo("davyvanroy");
        assertThat(result.getBody()).contains("<h1>body</h1>");
    }
}