package io.fundrequest.platform.tweb.profile;

import io.fundrequest.common.infrastructure.AbstractControllerTest;
import io.fundrequest.core.message.MessageService;
import io.fundrequest.core.message.domain.MessageType;
import io.fundrequest.core.message.dto.MessageDto;
import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.profile.github.GithubBountyService;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.GithubVerificationDto;
import io.fundrequest.platform.profile.ref.ReferralService;
import io.fundrequest.platform.profile.stackoverflow.StackOverflowBountyService;
import io.fundrequest.platform.profile.stackoverflow.dto.StackOverflowVerificationDto;
import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.context.ApplicationEventPublisher;

import java.security.Principal;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProfileControllerTest extends AbstractControllerTest<ProfileController> {

    private ReferralService referralService;
    private MessageService messageService;
    private Principal principal;
    private GithubBountyService githubBountyService;
    private StackOverflowBountyService stackOverflowBountyService;
    private ApplicationEventPublisher eventPublisher;
    private ProfileService profileService;

    @Override
    protected ProfileController setupController() {
        principal = () -> "davyvanroy";
        referralService = mock(ReferralService.class);
        messageService = mock(MessageService.class);
        when(referralService.generateRefLink(eq(principal.getName()), anyString())).thenReturn("ref");
        githubBountyService = mock(GithubBountyService.class);
        stackOverflowBountyService = mock(StackOverflowBountyService.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        profileService = mock(ProfileService.class);
        return new ProfileController(eventPublisher,
                                     profileService,
                                     messageService,
                                     referralService,
                                     githubBountyService,
                                     "staging",
                                     stackOverflowBountyService
        );
    }

    @Test
    void showProfile() throws Exception {
        MessageDto messageDto1 = MessageDto.builder()
                                           .type(MessageType.REFERRAL_SHARE)
                                           .name("twitter")
                                           .title("title1")
                                           .description("desc1")
                                           .link("https://areflinktotwitter.com")
                                           .build();
        MessageDto messageDto2 = MessageDto.builder()
                                           .type(MessageType.REFERRAL_SHARE)
                                           .name("twitter")
                                           .title("title1")
                                           .description("desc1")
                                           .link("https://areflinktotwitter.com")
                                           .build();
        MessageDto messageDto3 = MessageDto.builder()
                                           .type(MessageType.REFERRAL_SHARE)
                                           .name("twitter")
                                           .title("title1")
                                           .description("desc1")
                                           .link("https://areflinktotwitter.com")
                                           .build();

        when(githubBountyService.getVerification(principal)).thenReturn(GithubVerificationDto.builder().approved(true).build());
        when(stackOverflowBountyService.getVerification(principal)).thenReturn(StackOverflowVerificationDto.builder().approved(true).build());
        when(messageService.getMessageByKey("REFERRAL_SHARE.twitter")).thenReturn(messageDto1);
        when(messageService.getMessageByKey("REFERRAL_SHARE.linkedin")).thenReturn(messageDto2);
        when(messageService.getMessageByKey("REFERRAL_SHARE.facebook")).thenReturn(messageDto3);

        mockMvc.perform(get("/profile").principal(principal))
               .andExpect(status().isOk())
               .andExpect(model().attribute("isVerifiedGithub", true))
               .andExpect(model().attribute("isVerifiedStackOverflow", true))
               .andExpect(model().attribute("refLink", "ref"))
               .andExpect(model().attribute("refShareTwitter", messageDto1))
               .andExpect(model().attribute("refShareLinkedin", messageDto2))
               .andExpect(model().attribute("refShareFacebook", messageDto3));
    }

    @Test
    void manageWallets() throws Exception {
        KeycloakAuthenticationToken principal = mock(KeycloakAuthenticationToken.class);
        when(profileService.getArkaneAccessToken(principal)).thenReturn("token");
        mockMvc.perform(get("/profile/managewallets")
                                .principal(principal))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("https://connect-staging.arkane.network/wallets/manage?redirectUri=%2Fprofile&data=eyJjaGFpbiI6ICJldGhlcmV1bSJ9&bearerToken=token"));
    }

    @Test
    void linkProvider() throws Exception {
        KeycloakAuthenticationToken principal = mock(KeycloakAuthenticationToken.class);
        when(profileService.getArkaneAccessToken(principal)).thenReturn("token");
        when(profileService.createSignupLink(any(), eq(principal), eq(Provider.ARKANE), anyString()))
                .thenReturn("/signup");
        mockMvc.perform(get("/profile/link/arkane")
                                .principal(principal))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/signup"));
    }

    @Test
    void afterLinkProvider() throws Exception {
        KeycloakAuthenticationToken principal = mock(KeycloakAuthenticationToken.class);
        when(profileService.getArkaneAccessToken(principal)).thenReturn("token");

        mockMvc.perform(get("/profile/link/arkane/redirect?redirectUrl=fundrequest.io")
                                .principal(principal))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("fundrequest.io"));

        verify(profileService).userProviderIdentityLinked(principal, Provider.ARKANE);
    }

    @Test
    void afterLinkProviderNoRedirect() throws Exception {
        KeycloakAuthenticationToken principal = mock(KeycloakAuthenticationToken.class);
        when(profileService.getArkaneAccessToken(principal)).thenReturn("token");

        mockMvc.perform(get("/profile/link/arkane/redirect")
                                .principal(principal))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/profile"));

        verify(profileService).userProviderIdentityLinked(principal, Provider.ARKANE);
    }

    @Test
    void updateHeadline() throws Exception {
        KeycloakAuthenticationToken principal = mock(KeycloakAuthenticationToken.class);
        when(profileService.getArkaneAccessToken(principal)).thenReturn("token");

        mockMvc.perform(post("/profile/headline")
                .param("headline", "headline")
                                .principal(principal))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/profile"));

        verify(profileService).updateHeadline(principal, "headline");
    }
}