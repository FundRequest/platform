package io.fundreqest.platform.tweb.profile;

import io.fundreqest.platform.tweb.infrastructure.AbstractControllerTest;
import io.fundrequest.platform.faq.FAQService;
import io.fundrequest.platform.faq.model.FaqItemDto;
import io.fundrequest.platform.profile.github.GithubBountyService;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.GithubVerificationDto;
import io.fundrequest.platform.profile.ref.RefSignupEvent;
import io.fundrequest.platform.profile.ref.ReferralService;
import io.fundrequest.platform.profile.stackoverflow.StackOverflowBountyService;
import io.fundrequest.platform.profile.stackoverflow.dto.StackOverflowVerificationDto;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Same;
import org.springframework.context.ApplicationEventPublisher;

import java.security.Principal;
import java.util.ArrayList;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileControllerTest extends AbstractControllerTest<ProfileController> {

    private ReferralService referralService;
    private Principal principal;
    private GithubBountyService githubBountyService;
    private StackOverflowBountyService stackOverflowBountyService;
    private ApplicationEventPublisher eventPublisher;
    private FAQService faqService;

    @Override
    protected ProfileController setupController() {
        principal = () -> "davyvanroy";
        referralService = mock(ReferralService.class);
        when(referralService.generateRefLink(eq(principal.getName()), anyString())).thenReturn("ref");
        githubBountyService = mock(GithubBountyService.class);
        stackOverflowBountyService = mock(StackOverflowBountyService.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        faqService = mock(FAQService.class);
        return new ProfileController(eventPublisher,
                                     mock(ProfileService.class),
                                     referralService,
                                     githubBountyService,
                                     stackOverflowBountyService,
                                     faqService
        );
    }

    @Test
    void showProfile() throws Exception {
        final ArrayList<FaqItemDto> faqs = new ArrayList<>();

        when(githubBountyService.getVerification(principal)).thenReturn(GithubVerificationDto.builder().approved(true).build());
        when(stackOverflowBountyService.getVerification(principal)).thenReturn(StackOverflowVerificationDto.builder().approved(true).build());
        when(faqService.getFAQsForPage("profile")).thenReturn(faqs);

        mockMvc.perform(get("/profile").principal(principal))
               .andExpect(status().isOk())
               .andExpect(model().attribute("isVerifiedGithub", true))
               .andExpect(model().attribute("isVerifiedStackOverflow", true))
               .andExpect(model().attribute("refLink", "ref"))
               .andExpect(model().attribute("refLinkTwitter", "ref"))
               .andExpect(model().attribute("refLinkLinkedin", "ref"))
               .andExpect(model().attribute("refLinkFacebook", "ref"))
               .andExpect(model().attribute("faqs", new Same(faqs)));
    }

    @Test
    void showProfileEmitsEventWhenRefPresent() throws Exception {
        String ref = "ref";

        mockMvc.perform(get("/profile?ref=ref").principal(principal))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/profile"));

        RefSignupEvent expected = RefSignupEvent.builder().principal(principal).ref(ref).build();
        verify(eventPublisher).publishEvent(expected);
    }
}