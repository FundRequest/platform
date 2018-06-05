package io.fundreqest.platform.tweb.fund;

import io.fundreqest.platform.tweb.infrastructure.AbstractControllerTest;
import io.fundrequest.core.contract.service.FundRequestContractsService;
import io.fundrequest.core.request.fund.RefundService;
import io.fundrequest.core.request.fund.command.RequestRefundCommand;
import org.junit.jupiter.api.Test;

import java.security.Principal;

import static org.mockito.Matchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FundRestControllerTest extends AbstractControllerTest<FundRestController> {

    private FundRequestContractsService fundRequestContractsService;
    private RefundService refundService;
    private Principal principal;

    @Override
    protected FundRestController setupController() {
        principal = mock(Principal.class);
        when(principal.getName()).thenReturn("johndoe@someprovider.io");
        fundRequestContractsService = mock(FundRequestContractsService.class);
        refundService = mock(RefundService.class);
        return new FundRestController(fundRequestContractsService, refundService);
    }

    @Test
    void refund() throws Exception {
        final long requestId = 63L;
        final String funderaddress = "0x0srdjfgh2";
        final String r = "0xhjfghkjlkuiyguf1324";
        final String v = "0x0gfdhjgjhy23";
        final String s = "0xkjh786597hklj";

        this.mockMvc.perform(post("/rest/requests/{request-id}/refunds", requestId).contentType("application/json")
                                                                              .param("funderAddress", funderaddress)
                                                                              .param("r", r)
                                                                              .param("v", v)
                                                                              .param("s", s)
                                                                              .principal(principal))
                    .andExpect(status().isOk());

        verify(refundService).requestRefund(refEq(RequestRefundCommand.builder()
                                                                      .requestId(requestId)
                                                                      .funderAddress(funderaddress)
                                                                      .r(r)
                                                                      .v(v)
                                                                      .s(s)
                                                                      .build()));
    }
}