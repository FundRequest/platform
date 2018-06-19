package io.fundrequest.platform.tweb.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.dto.ClaimableResultDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.tweb.infrastructure.AbstractControllerTest;
import io.fundrequest.platform.tweb.request.dto.ClaimView;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static io.fundrequest.core.request.domain.Platform.GITHUB;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class RequestRestControllerTest extends AbstractControllerTest<RequestRestController> {

    private ObjectMapper objectMapper;
    private RequestService requestService;

    @Override
    protected RequestRestController setupController() {
        this.objectMapper = new ObjectMapper();
        requestService = mock(RequestService.class);
        return new RequestRestController(requestService);
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