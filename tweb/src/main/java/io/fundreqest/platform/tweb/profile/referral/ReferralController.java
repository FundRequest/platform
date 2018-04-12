package io.fundreqest.platform.tweb.profile.referral;

import io.fundrequest.platform.profile.ref.ReferralService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class ReferralController {

    private ReferralService referralService;

    public ReferralController(ReferralService referralService) {
        this.referralService = referralService;
    }

    @GetMapping("/referrals")
    public ModelAndView showReferrals(Principal principal) {
        ModelAndView mav = new ModelAndView("pages/profile/fragments/referrals");
        mav.addObject("referrals", referralService.getReferrals(principal));
        return mav;
    }

}