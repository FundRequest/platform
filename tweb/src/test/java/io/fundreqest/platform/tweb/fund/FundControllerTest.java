package io.fundreqest.platform.tweb.fund;

import io.fundreqest.platform.tweb.infrastructure.AbstractControllerTest;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.fund.RefundService;
import io.fundrequest.core.request.fund.command.RequestRefundCommand;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.junit.Test;

import java.security.Principal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class FundControllerTest extends AbstractControllerTest<FundController> {

    private Principal principal;
    private RequestService requestService;
    private RefundService refundService;
    private ProfileService profileService;

    @Override
    protected FundController setupController() {
        principal = mock(Principal.class);
        when(principal.getName()).thenReturn("davyvanroy@fundrequest.io");
        requestService = mock(RequestService.class);
        refundService = mock(RefundService.class);
        profileService = mock(ProfileService.class);
        return new FundController(requestService, refundService, profileService);
    }

    @Test
    public void fundSpecific() throws Exception {
        when(requestService.findRequest(1L)).thenReturn(RequestDtoMother.freeCodeCampNoUserStories());

        mockMvc.perform(get("/requests/{request-id}/fund", 1L).principal(principal))
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("url", "https://github.com/kazuki43zoo/api-stub/issues/42"))
                    .andExpect(view().name("pages/fund/github"));
    }

    @Test
    public void fund() throws Exception {
        mockMvc.perform(get("/fund/{type}", "github").principal(principal))
                    .andExpect(status().isOk())
                    .andExpect(view().name("pages/fund/github"));
    }

    @Test
    public void requestRefund() throws Exception {
        final long requestId = 38L;
        final String funderAddress = "0x24356789";

        when(profileService.getUserProfile(principal)).thenReturn(UserProfile.builder().etherAddress(funderAddress).etherAddressVerified(true).build());

        mockMvc.perform(post("/requests/{request-id}/refunds", requestId).principal(principal).param("funder_address", funderAddress))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectAlert("success", "Your refund has been requested and is waiting for approval."))
               .andExpect(redirectedUrl("/requests/38#funded-by"));

        verify(refundService).requestRefund(RequestRefundCommand.builder().requestId(requestId).funderAddress(funderAddress).requestedBy(principal.getName()).build());
    }

    @Test
    public void requestRefund_notFunder() throws Exception {
        final long requestId = 38L;
        final String funderAddress = "0x24356789";

        when(profileService.getUserProfile(principal)).thenReturn(UserProfile.builder().etherAddress("0x75636463").etherAddressVerified(true).build());

        mockMvc.perform(post("/requests/{request-id}/refunds", requestId).principal(principal).param("funder_address", funderAddress))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectAlert("danger", "Your request for a refund is not allowed because the address used to fund does not match the address on your profile."))
               .andExpect(redirectedUrl("/requests/38#funded-by"));

        verifyZeroInteractions(refundService);
    }

    @Test
    public void requestRefund_notAddressNotVerified() throws Exception {
        final long requestId = 38L;
        final String funderAddress = "0x24356789";

        when(profileService.getUserProfile(principal)).thenReturn(UserProfile.builder().etherAddress(funderAddress).etherAddressVerified(false).build());

        mockMvc.perform(post("/requests/{request-id}/refunds", requestId).principal(principal).param("funder_address", funderAddress))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectAlert("danger",
                                        "You need to validate your ETH address before you can request refunds. You can do this on your <a href='/profile'>profile</a> page."))
               .andExpect(redirectedUrl("/requests/38#funded-by"));

        verifyZeroInteractions(refundService);
    }
}