package io.fundrequest.platform.tweb.profile;

import io.fundrequest.common.infrastructure.AbstractControllerTest;
import io.fundrequest.core.message.MessageService;
import io.fundrequest.core.message.domain.MessageType;
import io.fundrequest.core.message.dto.MessageDto;
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
    private MessageService messageService;
    private Principal principal;
    private GithubBountyService githubBountyService;
    private StackOverflowBountyService stackOverflowBountyService;
    private ApplicationEventPublisher eventPublisher;

    @Override
    protected ProfileController setupController() {
        principal = () -> "davyvanroy";
        referralService = mock(ReferralService.class);
        messageService = mock(MessageService.class);
        when(referralService.generateRefLink(eq(principal.getName()), anyString())).thenReturn("ref");
        githubBountyService = mock(GithubBountyService.class);
        stackOverflowBountyService = mock(StackOverflowBountyService.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        return new ProfileController(eventPublisher,
                mock(ProfileService.class),
                messageService,
                referralService,
                githubBountyService,
                stackOverflowBountyService
        );
    }

    @Test
    void showProfile() throws Exception {
        when(githubBountyService.getVerification(principal)).thenReturn(GithubVerificationDto.builder().approved(true).build());
        when(stackOverflowBountyService.getVerification(principal)).thenReturn(StackOverflowVerificationDto.builder().approved(true).build());
        when(messageService.getMessageByKey("REFERRAL_SHARE.socialshare")).thenReturn(MessageDto.builder().type(MessageType.REFERRAL_SHARE).name("socialshare").build());

        mockMvc.perform(get("/profile").principal(principal))
                .andExpect(status().isOk())
                .andExpect(model().attribute("isVerifiedGithub", true))
                .andExpect(model().attribute("isVerifiedStackOverflow", true))
                .andExpect(model().attribute("refLink", "ref"))
                .andExpect(model().attribute("refShareTwitter", "ref"))
                .andExpect(model().attribute("refShareLinkedin", "ref"))
                .andExpect(model().attribute("refShareFacebook", "ref"));
    }

}