package io.fundreqest.platform.tweb.profile.bounty;

import io.fundrequest.platform.profile.bounty.service.BountyService;
import io.fundrequest.platform.profile.github.GithubBountyService;
import io.fundrequest.platform.profile.linkedin.LinkedInService;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.stackoverflow.StackOverflowBountyService;
import io.fundrequest.platform.profile.survey.domain.SurveyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class BountyController {

    private SurveyService surveyService;
    private GithubBountyService githubBountyService;
    private StackOverflowBountyService stackOverflowBountyService;
    private BountyService bountyService;
    private ProfileService profileService;
    private LinkedInService linkedInService;

    public BountyController(final SurveyService surveyService,
                            final GithubBountyService githubBountyService,
                            final StackOverflowBountyService stackOverflowBountyService,
                            final BountyService bountyService,
                            final ProfileService profileService,
                            LinkedInService linkedInService) {
        this.surveyService = surveyService;
        this.githubBountyService = githubBountyService;
        this.stackOverflowBountyService = stackOverflowBountyService;
        this.bountyService = bountyService;
        this.linkedInService = linkedInService;
        this.profileService = profileService;
    }

    @RequestMapping("/profile/rewards")
    public ModelAndView rewards(Principal principal) {
        ModelAndView mav = new ModelAndView("pages/profile/rewards");
        mav.addObject("survey", surveyService.getSurveyResult(principal));
        mav.addObject("githubVerification", githubBountyService.getVerification(principal));
        mav.addObject("stackOverflowVerification", stackOverflowBountyService.getVerification(principal));
        mav.addObject("linkedInVerification", linkedInService.getVerification(principal));
        mav.addObject("profile", profileService.getUserProfile(principal));
        mav.addObject("bounty", bountyService.getBounties(principal));
        return mav;
    }
}
