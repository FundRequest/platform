package io.fundreqest.platform.tweb.request.dto;

import io.fundreqest.platform.tweb.infrastructure.mav.EnumToCapitalizedStringMapper;
import io.fundrequest.core.request.view.IssueInformationDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.github.GithubGateway;
import io.fundrequest.platform.github.parser.GithubResult;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestDetailsViewMapperDecoratorTest {

    private RequestDetailsViewMapperDecorator decorator;
    private EnumToCapitalizedStringMapper enumToCapitalizedStringMapper;
    private RequestDetailsViewMapper delegate;
    private GithubGateway githubGateway;

    @Before
    public void setUp() {
        delegate = mock(RequestDetailsViewMapper.class);
        githubGateway = mock(GithubGateway.class);
        enumToCapitalizedStringMapper = mock(EnumToCapitalizedStringMapper.class);

        decorator = new RequestDetailsViewMapperDecorator() {};
        ReflectionTestUtils.setField(decorator, "delegate", delegate);
        ReflectionTestUtils.setField(decorator, "githubGateway", githubGateway);
        ReflectionTestUtils.setField(decorator, "enumToCapitalizedStringMapper", enumToCapitalizedStringMapper);
    }

    @Test
    public void map() {
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        final IssueInformationDto issueInformation = requestDto.getIssueInformation();
        final String status = "rdfjcgv";
        final GithubResult githubResult = new GithubResult();
        githubResult.setBody("<html/>");

        when(delegate.map(requestDto)).thenReturn(new RequestDetailsView());
        when(githubGateway.getIssue(issueInformation.getOwner(), issueInformation.getRepo(), issueInformation.getNumber())).thenReturn(githubResult);
        when(enumToCapitalizedStringMapper.map(requestDto.getStatus())).thenReturn(status);

        final RequestDetailsView result = decorator.map(requestDto);

        assertThat(result.getIcon()).isEqualTo("https://github.com/" + issueInformation.getOwner() + ".png");
        assertThat(result.getPlatform()).isEqualTo(issueInformation.getPlatform().name());
        assertThat(result.getOwner()).isEqualTo(issueInformation.getOwner());
        assertThat(result.getRepo()).isEqualTo(issueInformation.getRepo());
        assertThat(result.getIssueNumber()).isEqualTo(issueInformation.getNumber());
        assertThat(result.getTitle()).isEqualTo(issueInformation.getTitle());
        assertThat(result.getStarred()).isEqualTo(requestDto.isLoggedInUserIsWatcher());
        assertThat(result.getStatus()).isEqualTo(status);
        assertThat(result.getDescription()).isEqualTo(githubResult.getBody());
    }

    @Test
    public void map_null() {
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();

        when(delegate.map(requestDto)).thenReturn(null);

        final RequestDetailsView result = decorator.map(requestDto);

        assertThat(result).isNull();
    }
}
