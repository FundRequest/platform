package io.fundreqest.platform.tweb.fund;

import io.fundreqest.platform.tweb.infrastructure.AbstractControllerTest;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.fund.RefundService;
import io.fundrequest.core.request.fund.command.RequestRefundCommand;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.junit.Test;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;

import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.APPROVED;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.PENDING;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
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
        final RequestDto request = RequestDtoMother.fundRequestArea51();
        request.setStatus(RequestStatus.FUNDED);
        final String funderAddress = "0x24356789";

        when(profileService.getUserProfile(principal)).thenReturn(UserProfile.builder().etherAddress(funderAddress).etherAddressVerified(true).build());
        when(requestService.findRequest(requestId)).thenReturn(request);
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0xeab43f").build()));

        mockMvc.perform(post("/requests/{request-id}/refunds", requestId).principal(principal).param("funder_address", funderAddress))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectAlert("success", "Your refund has been requested and is waiting for approval."))
               .andExpect(redirectedUrl("/requests/38#details"));

        verify(refundService).requestRefund(RequestRefundCommand.builder().requestId(requestId).funderAddress(funderAddress).requestedBy(principal.getName()).build());
    }

    @Test
    public void requestRefund_notFunder() throws Exception {
        final long requestId = 38L;
        final RequestDto request = RequestDtoMother.fundRequestArea51();
        request.setStatus(RequestStatus.FUNDED);
        final String funderAddress = "0x24356789";

        when(profileService.getUserProfile(principal)).thenReturn(UserProfile.builder().etherAddress("0x75636463").etherAddressVerified(true).build());
        when(requestService.findRequest(requestId)).thenReturn(request);
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0xeab43f").build()));

        mockMvc.perform(post("/requests/{request-id}/refunds", requestId).principal(principal).param("funder_address", funderAddress))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectAlert("danger", "Your request for a refund is not allowed because the address used to fund does not match the address on your profile."))
               .andExpect(redirectedUrl("/requests/38#details"));

        verify(refundService).findAllRefundRequestsFor(requestId, PENDING, APPROVED);
        verifyNoMoreInteractions(refundService);
    }

    @Test
    public void requestRefund_notAddressNotVerified() throws Exception {
        final long requestId = 38L;
        final RequestDto request = RequestDtoMother.fundRequestArea51();
        request.setStatus(RequestStatus.FUNDED);
        final String funderAddress = "0x24356789";

        when(profileService.getUserProfile(principal)).thenReturn(UserProfile.builder().etherAddress(funderAddress).etherAddressVerified(false).build());
        when(requestService.findRequest(requestId)).thenReturn(request);
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0xeab43f").build()));

        mockMvc.perform(post("/requests/{request-id}/refunds", requestId).principal(principal).param("funder_address", funderAddress))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectAlert("danger",
                                        "You need to validate your ETH address before you can request refunds. You can do this on your <a href='/profile'>profile</a> page."))
               .andExpect(redirectedUrl("/requests/38#details"));

        verify(refundService).findAllRefundRequestsFor(requestId, PENDING, APPROVED);
        verifyNoMoreInteractions(refundService);
    }

    @Test
    public void requestRefund_statusNotFunded() throws Exception {
        final long requestId = 38L;
        final RequestDto request = RequestDtoMother.fundRequestArea51();
        request.setStatus(RequestStatus.CLAIM_REQUESTED);
        final String funderAddress = "0x24356789";

        when(profileService.getUserProfile(principal)).thenReturn(UserProfile.builder().etherAddress(funderAddress).etherAddressVerified(true).build());
        when(requestService.findRequest(requestId)).thenReturn(request);
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Collections.singletonList(RefundRequestDto.builder().funderAddress("0xeab43f").build()));

        mockMvc.perform(post("/requests/{request-id}/refunds", requestId).principal(principal).param("funder_address", funderAddress))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectAlert("danger",
                                        "The status of the request needs to be 'Funded' to be able to ask a refund"))
               .andExpect(redirectedUrl("/requests/38#details"));

        verify(refundService).findAllRefundRequestsFor(requestId, PENDING, APPROVED);
        verifyNoMoreInteractions(refundService);
    }

    @Test
    public void requestRefund_refundAlreadyRequested() throws Exception {
        final long requestId = 38L;
        final RequestDto request = RequestDtoMother.fundRequestArea51();
        request.setStatus(RequestStatus.FUNDED);
        final String funderAddress = "0x24356789";

        when(profileService.getUserProfile(principal)).thenReturn(UserProfile.builder().etherAddress(funderAddress).etherAddressVerified(true).build());
        when(requestService.findRequest(requestId)).thenReturn(request);
        when(refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)).thenReturn(Arrays.asList(RefundRequestDto.builder().funderAddress("0xeab43f").build(),
                                                                                                            RefundRequestDto.builder().funderAddress(funderAddress).build()));

        mockMvc.perform(post("/requests/{request-id}/refunds", requestId).principal(principal).param("funder_address", funderAddress))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectAlert("danger",
                                        "A refund for this issue and address has already been requested"))
               .andExpect(redirectedUrl("/requests/38#details"));

        verify(refundService).findAllRefundRequestsFor(requestId, PENDING, APPROVED);
        verifyNoMoreInteractions(refundService);
    }
}