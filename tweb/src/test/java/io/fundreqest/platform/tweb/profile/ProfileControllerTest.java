package io.fundreqest.platform.tweb.profile;

import io.fundreqest.platform.tweb.infrastructure.AbstractControllerTest;
import io.fundrequest.platform.profile.github.GithubBountyService;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.GithubVerificationDto;
import io.fundrequest.platform.profile.ref.ReferralService;
import io.fundrequest.platform.profile.stackoverflow.StackOverflowBountyService;
import io.fundrequest.platform.profile.stackoverflow.dto.StackOverflowVerificationDto;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import java.security.Principal;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileControllerTest extends AbstractControllerTest<ProfileController> {

    private ReferralService referralService;
    private Principal principal;
    private GithubBountyService githubBountyService;
    private StackOverflowBountyService stackOverflowBountyService;
    private ApplicationEventPublisher eventPublisher;

    @Override
    protected ProfileController setupController() {
        principal = () -> "davyvanroy";
        referralService = mock(ReferralService.class);
        when(referralService.generateRefLink(eq(principal.getName()), anyString())).thenReturn("ref");
        githubBountyService = mock(GithubBountyService.class);
        stackOverflowBountyService = mock(StackOverflowBountyService.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        return new ProfileController(eventPublisher,
                mock(ProfileService.class),
                referralService,
                githubBountyService,
                stackOverflowBountyService
        );
    }

    @Test
    void showProfile() throws Exception {
        when(githubBountyService.getVerification(principal)).thenReturn(GithubVerificationDto.builder().approved(true).build());
        when(stackOverflowBountyService.getVerification(principal)).thenReturn(StackOverflowVerificationDto.builder().approved(true).build());

        mockMvc.perform(get("/profile").principal(principal))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isVerifiedGithub", true))
                .andExpect(model().attribute("isVerifiedStackOverflow", true))
                .andExpect(model().attribute("refLink", "ref"))
                .andExpect(model().attribute("refLinkTwitter", "ref"))
                .andExpect(model().attribute("refLinkLinkedin", "ref"))
                .andExpect(model().attribute("refLinkFacebook", "ref"));
    }

}