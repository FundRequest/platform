package io.fundreqest.platform.tweb.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundreqest.platform.tweb.infrastructure.AbstractControllerTest;
import io.fundreqest.platform.tweb.request.dto.RequestView;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.ClaimService;
import io.fundrequest.core.request.claim.dto.UserClaimableDto;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.PendingFundService;
import io.fundrequest.core.request.statistics.StatisticsService;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.profile.profile.ProfileService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

public class RequestControllerTest extends AbstractControllerTest<RequestController> {

    private Principal principal;
    private RequestService requestService;
    private ProfileService profileService;
    private Mappers mappers;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        principal = mock(Principal.class);
        when(principal.getName()).thenReturn("davyvanroy@fundrequest.io");
    }

    @Override
    protected RequestController setupController() {
        requestService = mock(RequestService.class);
        profileService = mock(ProfileService.class, RETURNS_DEEP_STUBS);
        mappers = mock(Mappers.class);
        objectMapper = spy(new ObjectMapper());
        return new RequestController(requestService,
                mock(PendingFundService.class),
                mock(StatisticsService.class),
                profileService,
                mock(FundService.class),
                mock(ClaimService.class),
                objectMapper,
                mappers);
    }

    @Test
    public void detailActions() throws Exception {
        RequestDto request = RequestDtoMother.freeCodeCampNoUserStories();
        when(requestService.findRequest(1L)).thenReturn(request);
        UserClaimableDto userClaimableDto = UserClaimableDto.builder().build();
        when(requestService.getUserClaimableResult(principal, 1L)).thenReturn(userClaimableDto);

        this.mockMvc.perform(get("/requests/{id}/actions", 1L).principal(principal))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("pages/requests/detail-actions :: details"))
                .andExpect(MockMvcResultMatchers.model().attribute("request", request))
                .andExpect(MockMvcResultMatchers.model().attribute("userClaimable", userClaimableDto));
    }

    @Test
    public void claimRequest() throws Exception {
        RequestDto request = RequestDtoMother.freeCodeCampNoUserStories();
        when(requestService.findRequest(1L)).thenReturn(request);
        UserClaimableDto userClaimableDto = UserClaimableDto.builder().build();
        when(requestService.getUserClaimableResult(principal, 1L)).thenReturn(userClaimableDto);
        when(profileService.getUserProfile(principal.getName()).getEtherAddress()).thenReturn("0x0000000");

        this.mockMvc.perform(post("/requests/{id}/claim", 1L).principal(principal))
                .andExpect(redirectAlert("success", "Your claim has been requested and waiting for approval."))
                .andExpect(redirectedUrl("/requests/" + 1L));
    }

    @Test
    public void claimRequestShowsErrorMsg() throws Exception {
        when(profileService.getUserProfile(principal.getName()).getEtherAddress()).thenReturn("");

        this.mockMvc.perform(post("/requests/{id}/claim", 1L).principal(principal))
                .andExpect(redirectAlert("danger", "Please update your profile with a correct ether address."))
                .andExpect(redirectedUrl("/requests/" + 1L));
    }

    @Test
    public void toggleWatchRequest() throws Exception {
        RequestDto request = RequestDtoMother.freeCodeCampNoUserStories();
        RequestView requestView = new RequestView();
        when(requestService.findRequest(1L)).thenReturn(request);
        when(mappers.map(RequestDto.class, RequestView.class, request)).thenReturn(requestView);
        when(objectMapper.writeValueAsString(requestView)).thenReturn("{ starred: true }");

        this.mockMvc.perform(post("/requests/{id}/watch", 1L).principal(principal))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{ starred: true }"));
    }

}