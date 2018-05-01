package io.fundreqest.platform.tweb.fund;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.view.RequestDtoMother;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class FundControllerTest {

    private MockMvc mockMvc;
    private Principal principal;
    private RequestService requestService;
    private FundController controller;

    @Before
    public void setUp() throws Exception {
        principal = mock(Principal.class);
        when(principal.getName()).thenReturn("davyvanroy@fundrequest.io");
        requestService = mock(RequestService.class);
        controller = new FundController(requestService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void fundSpecific() throws Exception {
        when(requestService.findRequest(1L)).thenReturn(RequestDtoMother.freeCodeCampNoUserStories());

        this.mockMvc.perform(get("/requests/{request-id}/fund", 1L).principal(principal))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("pages/fund/github"))
                    .andExpect(MockMvcResultMatchers.model().attribute("url", "https://github.com/kazuki43zoo/api-stub/issues/42"));
    }

    @Test
    public void fund() throws Exception {
        this.mockMvc.perform(get("/fund/{type}", "github").principal(principal))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.view().name("pages/fund/github"));
    }
}