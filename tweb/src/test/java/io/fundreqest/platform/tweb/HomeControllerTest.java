package io.fundreqest.platform.tweb;

import io.fundreqest.platform.tweb.infrastructure.AbstractControllerTest;
import io.fundrequest.core.PrincipalMother;
import io.fundrequest.platform.profile.profile.ProfileService;
import org.junit.Test;

import java.security.Principal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class HomeControllerTest extends AbstractControllerTest<HomeController> {

    private ProfileService profileService;

    @Override
    protected HomeController setupController() {
        profileService = mock(ProfileService.class);
        return new HomeController(profileService);
    }

    @Test
    public void home() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("index"));
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