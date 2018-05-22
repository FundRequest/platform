package io.fundreqest.platform.tweb.fund;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.platform.faq.FAQService;
import io.fundrequest.platform.faq.model.FaqItemDto;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.Same;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public class FundControllerTest {

    private MockMvc mockMvc;
    private Principal principal;
    private RequestService requestService;
    private FundController controller;
    private FAQService faqService;

    @Before
    public void setUp() throws Exception {
        principal = mock(Principal.class);
        when(principal.getName()).thenReturn("davyvanroy@fundrequest.io");
        requestService = mock(RequestService.class);
        faqService = mock(FAQService.class);
        controller = new FundController(requestService, faqService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void fundSpecific() throws Exception {
        final List<FaqItemDto> faqs = new ArrayList<>();

        when(requestService.findRequest(1L)).thenReturn(RequestDtoMother.freeCodeCampNoUserStories());
        when(faqService.getFAQsForPage("fundGithub")).thenReturn(faqs);

        this.mockMvc.perform(get("/requests/{request-id}/fund", 1L).principal(principal))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.model().attribute("url", "https://github.com/kazuki43zoo/api-stub/issues/42"))
                    .andExpect(MockMvcResultMatchers.model().attribute("faqs", new Same(faqs)))
                    .andExpect(MockMvcResultMatchers.view().name("pages/fund/github"));
    }

    @Test
    public void fund() throws Exception {
        final List<FaqItemDto> faqs = new ArrayList<>();

        when(faqService.getFAQsForPage("fundGithub")).thenReturn(faqs);

        this.mockMvc.perform(get("/fund/{type}", "github").principal(principal))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.model().attribute("faqs", new Same(faqs)))
                    .andExpect(MockMvcResultMatchers.view().name("pages/fund/github"));
    }
}