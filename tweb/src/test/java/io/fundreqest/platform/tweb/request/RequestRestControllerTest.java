package io.fundreqest.platform.tweb.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundreqest.platform.tweb.infrastructure.AbstractControllerTest;
import io.fundreqest.platform.tweb.infrastructure.mav.EnumToCapitalizedStringMapper;
import io.fundreqest.platform.tweb.request.dto.ClaimView;
import io.fundreqest.platform.tweb.request.dto.RequestView;
import io.fundreqest.platform.tweb.request.dto.RequestViewMapper;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.dto.ClaimableResultDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static io.fundrequest.core.request.domain.Platform.GITHUB;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

class RequestRestControllerTest extends AbstractControllerTest<RequestRestController> {

    private ObjectMapper objectMapper;
    private Mappers mappers;
    private RequestViewMapper mapper;
    private EnumToCapitalizedStringMapper enumToCapitalizedStringMapper;
    private RequestService requestService;

    @Override
    protected RequestRestController setupController() {
        this.objectMapper = new ObjectMapper();
        this.mappers = mock(Mappers.class);
        this.requestService = mock(RequestService.class);
        this.enumToCapitalizedStringMapper = mock(EnumToCapitalizedStringMapper.class);
        this.mapper = new RequestViewMapper(enumToCapitalizedStringMapper);
        return new RequestRestController(requestService, mappers);
    }

    @Test
    void requestDetails() throws Exception {
        final String owner = "fundrequest";
        final String repo = "platform";
        final String issueNumber = "320";
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        final RequestView requestView = mapper.map(requestDto);

        final String platformId = owner + "|FR|" + repo + "|FR|" + issueNumber;
        when(requestService.findRequest(GITHUB, platformId)).thenReturn(requestDto);
        when(mappers.map(RequestDto.class, RequestView.class, requestDto)).thenReturn(requestView);

        mockMvc.perform(get("/rest/requests/github/{owner}/{repo}/{number}", owner, repo, issueNumber).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(requestView)));
    }

    @Test
    void claimDetails() throws Exception {
        final String owner = "fundrequest";
        final String repo = "platform";
        final String issueNumber = "320";
        final String claimableBy = "hgfgh";
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        final ClaimView claimView = new ClaimView(true, claimableBy);

        final String platformId = owner + "|FR|" + repo + "|FR|" + issueNumber;
        when(requestService.findRequest(GITHUB, platformId)).thenReturn(requestDto);
        when(requestService.getClaimableResult(requestDto.getId())).thenReturn(ClaimableResultDto.builder()
                                                                                                 .claimableByPlatformUserName(claimableBy)
                                                                                                 .claimable(true)
                                                                                                 .platform(GITHUB)
                                                                                                 .build());

        mockMvc.perform(get("/rest/requests/github/{owner}/{repo}/{number}/claimable", owner, repo, issueNumber).accept(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(claimView)));
    }

    @Test
    void claimDetails_notClaimable() throws Exception {
        final String owner = "fundrequest";
        final String repo = "platform";
        final String issueNumber = "320";
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        final ClaimView claimView = new ClaimView(false, null);

        final String platformId = owner + "|FR|" + repo + "|FR|" + issueNumber;
        when(requestService.findRequest(GITHUB, platformId)).thenReturn(requestDto);
        when(requestService.getClaimableResult(requestDto.getId())).thenReturn(ClaimableResultDto.builder()
                                                                                                 .claimable(false)
                                                                                                 .platform(GITHUB)
                                                                                                 .build());

        mockMvc.perform(get("/rest/requests/github/{owner}/{repo}/{number}/claimable", owner, repo, issueNumber).accept(MediaType.APPLICATION_JSON_UTF8))
               .andExpect(MockMvcResultMatchers.status().isOk())
               .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(claimView)));
    }
}