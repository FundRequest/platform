package io.fundreqest.platform.tweb;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundreqest.platform.tweb.infrastructure.AbstractControllerTest;
import io.fundrequest.core.PrincipalMother;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.ref.RefSignupEvent;
import org.junit.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.security.Principal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class HomeControllerTest extends AbstractControllerTest<HomeController> {

    private ProfileService profileService;
    private ApplicationEventPublisher eventPublisher;
    private RequestService requestService;
    private ObjectMapper objectMapper;
    private Mappers mappers;

    @Override
    protected HomeController setupController() {
        profileService = mock(ProfileService.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        requestService = mock(RequestService.class);
        objectMapper = mock(ObjectMapper.class);
        mappers = mock(Mappers.class);
        return new HomeController(profileService, eventPublisher, requestService, objectMapper, mappers);
    }

    @Test
    public void home() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("index"));
    }

    @Test
    public void homeEmitsReferral() throws Exception {
        Principal principal = () -> "davy";
        mockMvc.perform(get("/?ref=123").principal(principal))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/"));

        verify(eventPublisher).publishEvent(RefSignupEvent.builder().principal(principal).ref("123").build());
    }

    @Test
    public void login() throws Exception {
        mockMvc.perform(get("/user/login").header("referer", "localhost"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("localhost"));
    }

    @Test
    public void logoutWithPrincipal() throws Exception {
        Principal principal = PrincipalMother.davyvanroy();
        mockMvc.perform(get("/logout").principal(principal))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/"));

        verify(profileService).logout(principal);
    }

    @Test
    public void logoutWithoutPrincipal() throws Exception {
        mockMvc.perform(get("/logout"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/"));
    }
}