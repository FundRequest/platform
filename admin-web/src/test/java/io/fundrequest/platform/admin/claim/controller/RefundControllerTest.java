package io.fundrequest.platform.admin.claim.controller;

import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.platform.admin.infrastructure.GenericControllerAdvice;
import io.fundrequest.platform.admin.refund.RefundController;
import io.fundrequest.platform.admin.service.ModerationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Same;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class RefundControllerTest {

    protected MockMvc mockMvc;
    private Principal principal;
    private ModerationService<RefundRequestDto> refundModerationService;

    @BeforeEach
    public void setUpMockMvc() throws Exception {
        principal = mock(Principal.class);
        refundModerationService = mock(ModerationService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new RefundController(refundModerationService))
                                 .setControllerAdvice(new GenericControllerAdvice())
                                 .setViewResolvers(new InternalResourceViewResolver("/templates/", ".html"))
                                 .build();
    }

    @Test
    public void getRefunds() throws Exception {
        final List<RefundRequestDto> expectedPending = new ArrayList<>();
        final List<RefundRequestDto> expectedFailed = new ArrayList<>();

        when(refundModerationService.listPending()).thenReturn(expectedPending);
        when(refundModerationService.listFailed()).thenReturn(expectedFailed);

        mockMvc.perform(get("/refunds").principal(principal))
               .andExpect(model().attribute("pendingRefunds", new Same(expectedPending)))
               .andExpect(model().attribute("failedRefunds", new Same(expectedFailed)))
               .andExpect(view().name("refunds"));
    }

    @Test
    public void updateRefundRequest_APPROVE() throws Exception {
        final long refundRequestId = 543L;

        mockMvc.perform(put("/refunds/{refundRequestId}", refundRequestId).principal(principal)
                                                                          .contentType("application/json")
                                                                          .param("action", "APPROVE"));

        verify(refundModerationService).approve(refundRequestId);
    }

    @Test
    public void updateRefundRequest_DECLINE() throws Exception {
        final long refundRequestId = 432L;

        mockMvc.perform(put("/refunds/{refundRequestId}", refundRequestId).principal(principal)
                                                                          .contentType("application/json")
                                                                          .param("action", "DECLINE"));

        verify(refundModerationService).decline(refundRequestId);
    }

    @Test
    public void updateRefundRequest_invalidAction() throws Exception {
        final long refundRequestId = 432L;

        mockMvc.perform(put("/refunds/{refundRequestId}", refundRequestId).principal(principal)
                                                                          .contentType("application/json")
                                                                          .param("action", "GFHJ"))
               .andExpect(status().isUnprocessableEntity());

        verifyZeroInteractions(refundModerationService);
    }
}
