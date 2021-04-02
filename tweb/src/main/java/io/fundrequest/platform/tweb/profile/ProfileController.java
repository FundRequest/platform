package io.fundrequest.platform.tweb.profile;

import io.fundrequest.core.message.MessageService;
import io.fundrequest.core.message.dto.MessageDto;
import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.profile.github.GithubBountyService;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.GithubVerificationDto;
import io.fundrequest.platform.profile.ref.ReferralService;
import io.fundrequest.platform.profile.stackoverflow.StackOverflowBountyService;
import io.fundrequest.platform.profile.stackoverflow.dto.StackOverflowVerificationDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Base64;

@Controller
@Slf4j
public class ProfileController {
    private ApplicationEventPublisher eventPublisher;
    private ProfileService profileService;
    private MessageService messageService;
    private ReferralService referralService;
    private GithubBountyService githubBountyService;
    private String chain;
    private StackOverflowBountyService stackOverflowBountyService;
    private String arkaneEnvironment;


    public ProfileController(final ApplicationEventPublisher eventPublisher,
                             final ProfileService profileService,
                             final MessageService messageService,
                             final ReferralService referralService,
                             final GithubBountyService githubBountyService,
                             final @Value("${network.arkane.environment}") String arkaneEnvironment,
                             final @Value("${io.fundrequest.chain:ethereum}") String chain,
                             final StackOverflowBountyService stackOverflowBountyService) {
        this.eventPublisher = eventPublisher;
        this.profileService = profileService;
        this.messageService = messageService;
        this.referralService = referralService;
        this.githubBountyService = githubBountyService;
        this.arkaneEnvironment = arkaneEnvironment;
        this.chain = chain;
        this.stackOverflowBountyService = stackOverflowBountyService;
    }

    @GetMapping("/profile")
    public ModelAndView showProfile(Principal principal) throws Exception {
        final ModelAndView mav = new ModelAndView("pages/profile/index");

        mav.addObject("isVerifiedGithub", isVerifiedGithub(principal));
        mav.addObject("isVerifiedStackOverflow", isVerifiedStackOverflow(principal));
        mav.addObject("refLink", getRefLink(principal, "web"));
        mav.addObject("refShareTwitter", processRefLinkMessage(messageService.getMessageByKey("REFERRAL_SHARE.twitter"), getRefLink(principal, "twitter")));
        mav.addObject("refShareLinkedin", processRefLinkMessage(messageService.getMessageByKey("REFERRAL_SHARE.linkedin"), getRefLink(principal, "linkedin")));
        mav.addObject("refShareFacebook", processRefLinkMessage(messageService.getMessageByKey("REFERRAL_SHARE.facebook"), getRefLink(principal, "facebook")));
        mav.addObject("arkaneWalletEndpoint", getArkaneWalletEndpoint());

        return mav;
    }

    private MessageDto processRefLinkMessage(MessageDto message, String refLink) throws Exception {
        String link = message.getLink();
        String description = message.getDescription().replace("###REFLINK###", refLink);
        link = link
                .replace("###REFLINK###", URLEncoder.encode(refLink, "UTF-8"))
                .replace("###TITLE###", URLEncoder.encode(message.getTitle(), "UTF-8"))
                .replace("###DESCRIPTION###", URLEncoder.encode(description, "UTF-8"));
        message.setDescription(description);
        message.setLink(link);

        return message;
    }

    private boolean isVerifiedStackOverflow(final Principal principal) {
        final StackOverflowVerificationDto verification = stackOverflowBountyService.getVerification(principal);
        return verification != null && verification.isApproved();
    }

    private boolean isVerifiedGithub(final Principal principal) {
        final GithubVerificationDto verification = githubBountyService.getVerification(principal);
        return verification != null && verification.isApproved();
    }

    @GetMapping("/profile/link/{provider}")
    public ModelAndView linkProfile(@PathVariable String provider,
                                    HttpServletRequest request,
                                    Principal principal,
                                    @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        if (StringUtils.isBlank(redirectUrl)) {
            redirectUrl = request.getRequestURL().toString();
        }
        String link = profileService.createSignupLink(request, principal, Provider.fromString(provider.replaceAll("[^A-Za-z]", "")), redirectUrl);
        log.info("Redirecting to: {}", link);
        return new ModelAndView(new RedirectView(link, false));
    }

    @GetMapping("/profile/managewallets")
    public ModelAndView manageWallets(Principal principal, HttpServletRequest request) throws UnsupportedEncodingException {
        String bearerToken = URLEncoder.encode(profileService.getArkaneAccessToken((KeycloakAuthenticationToken) principal), "UTF-8");
        String redirectUri = request.getHeader("referer");
        if (redirectUri == null) {
            redirectUri = "/profile";
        }
        redirectUri = URLEncoder.encode(redirectUri, "UTF-8");
        String url = getConnectEndpoint(bearerToken, redirectUri);
        profileService.walletsManaged(principal);
        return new ModelAndView(new RedirectView(url));
    }

    private String getConnectEndpoint(String bearerToken, String redirectUri) {
        String endpoint = "https://connect.arkane.network";
        if (StringUtils.isNotBlank(arkaneEnvironment)) {
            endpoint = "https://connect-" + arkaneEnvironment + ".arkane.network";
        }
        String walletsFromChain = chain.toLowerCase();
        String data = Base64.getEncoder().encodeToString(("{\"chain\": \"" + walletsFromChain + "\"}").getBytes(StandardCharsets.UTF_8));
        return endpoint + "/wallets/manage?redirectUri=" + redirectUri + "&data=" + data + "&bearerToken=" + bearerToken;
    }

    private String getArkaneWalletEndpoint() {
        String endpoint = "https://app.arkane.network/chains/ethereum/wallets/";
        if (StringUtils.isNotBlank(arkaneEnvironment)) {
            endpoint = "https://" + arkaneEnvironment + ".arkane.network/chains/ethereum/wallets/";
        }
        return endpoint;
    }

    @PostMapping("/profile/headline")
    public ModelAndView updateHeadline(Principal principal, @RequestParam("headline") String headline) {
        profileService.updateHeadline(principal, headline);
        return redirectToProfile();
    }

    private String getRefLink(final Principal principal, String source) {
        return referralService.generateRefLink(principal.getName(), source);
    }

    @GetMapping("/profile/link/{provider}/redirect")
    public ModelAndView redirectToHereAfterProfileLink(Principal principal,
                                                       @PathVariable("provider") String provider,
                                                       @RequestParam(value = "redirectUrl", required = false) String redirectUrl) {
        profileService.userProviderIdentityLinked(principal, Provider.fromString(provider));

        if (StringUtils.isNotBlank(redirectUrl) && !redirectUrl.toLowerCase().contains("/profile/link/")) {
            return new ModelAndView(new RedirectView(redirectUrl));
        }
        return redirectToProfile();
    }

    private ModelAndView redirectToProfile() {
        return new ModelAndView(new RedirectView("/profile"));
    }

}
