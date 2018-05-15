package io.fundreqest.platform.tweb.request.dto;

import io.fundreqest.platform.tweb.infrastructure.mav.EnumToCapitalizedStringMapper;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RequestViewMapperTest {

    private RequestViewMapper mapper;

    private EnumToCapitalizedStringMapper enumToCapitalizedStringMapper;

    @Before
    public void setUp() {
        enumToCapitalizedStringMapper = mock(EnumToCapitalizedStringMapper.class);
        mapper = new RequestViewMapper(enumToCapitalizedStringMapper);
    }

    @Test
    public void map() {
        final String status = "szfgdhxf";
        final String fase = "sgzdxhfc";
        final RequestDto request = RequestDtoMother.fundRequestArea51();

        when(enumToCapitalizedStringMapper.map(request.getStatus())).thenReturn(status);
        when(enumToCapitalizedStringMapper.map(request.getStatus().getFase())).thenReturn(fase);

        final RequestView result = mapper.map(request);

        assertThat(result.getId()).isEqualTo(request.getId());
        assertThat(result.getIcon()).isEqualTo("https://github.com/" + request.getIssueInformation().getOwner() + ".png");
        assertThat(result.getOwner()).isEqualTo(request.getIssueInformation().getOwner());
        assertThat(result.getRepo()).isEqualTo(request.getIssueInformation().getRepo());
        assertThat(result.getIssueNumber()).isEqualTo(request.getIssueInformation().getNumber());
        assertThat(result.getPlatform()).isEqualTo(request.getIssueInformation().getPlatform().name());
        assertThat(result.getTitle()).isEqualTo(request.getIssueInformation().getTitle());
        assertThat(result.getStatus()).isEqualTo(status);
        assertThat(result.getFase()).isEqualTo(fase);
        assertThat(result.getStarred()).isEqualTo(request.isLoggedInUserIsWatcher());
        assertThat(result.getTechnologies()).isEqualTo(request.getTechnologies());
        assertThat(result.getFunds()).isEqualTo(request.getFunds());
        assertThat(result.getCreationDate()).isEqualTo(request.getCreationDate());
        assertThat(result.getLastModifiedDate()).isEqualTo(request.getLastModifiedDate());
    }

    @Test
    public void map_null() {
        assertThat(mapper.map(null)).isNull();
    }

}
