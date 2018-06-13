package io.fundreqest.platform.tweb.profile;

import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.profile.github.GithubBountyService;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.GithubVerificationDto;
import io.fundrequest.platform.profile.ref.ReferralService;
import io.fundrequest.platform.profile.stackoverflow.StackOverflowBountyService;
import io.fundrequest.platform.profile.stackoverflow.dto.StackOverflowVerificationDto;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.security.Principal;

@Controller
public class ProfileController {
    private ApplicationEventPublisher eventPublisher;
    private ProfileService profileService;
    private ReferralService referralService;
    private GithubBountyService githubBountyService;
    private StackOverflowBountyService stackOverflowBountyService;


    public ProfileController(final ApplicationEventPublisher eventPublisher,
                             final ProfileService profileService,
                             final ReferralService referralService,
                             final GithubBountyService githubBountyService,
                             final StackOverflowBountyService stackOverflowBountyService) {
        this.eventPublisher = eventPublisher;
        this.profileService = profileService;
        this.referralService = referralService;
        this.githubBountyService = githubBountyService;
        this.stackOverflowBountyService = stackOverflowBountyService;
    }

    @GetMapping("/profile")
    public ModelAndView showProfile(Principal principal) throws Exception {
        final ModelAndView mav = new ModelAndView("pages/profile/index");

        mav.addObject("isVerifiedGithub", isVerifiedGithub(principal));
        mav.addObject("isVerifiedStackOverflow", isVerifiedStackOverflow(principal));
        mav.addObject("refLink", getRefLink(principal, "web"));
        mav.addObject("refLinkTwitter", URLEncoder.encode(getRefLink(principal, "twitter"), "UTF-8"));
        mav.addObject("refLinkLinkedin", URLEncoder.encode(getRefLink(principal, "linkedin"), "UTF-8"));
        mav.addObject("refLinkFacebook", URLEncoder.encode(getRefLink(principal, "facebook"), "UTF-8"));
        return mav;
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
    public ModelAndView linkProfile(@PathVariable String provider, HttpServletRequest request, Principal principal) {
        String link = profileService.createSignupLink(request, principal, Provider.fromString(provider.replaceAll("[^A-Za-z]", "")));
        return new ModelAndView(new RedirectView(link, false));
    }

    @PostMapping("/profile/etheraddress")
    public ModelAndView updateAddress(Principal principal, @RequestParam("etheraddress") String etherAddress) {
        profileService.updateEtherAddress(principal, etherAddress);
        return redirectToProfile();
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
    public ModelAndView redirectToHereAfterProfileLink(Principal principal, @PathVariable("provider") String provider) {
        profileService.userProviderIdentityLinked(principal, Provider.fromString(provider));
        return redirectToProfile();
    }

    private ModelAndView redirectToProfile() {
        return new ModelAndView(new RedirectView("/profile"));
    }

}
