package io.fundrequest.platform.admin.claim.controller;

import io.fundrequest.platform.admin.claim.service.ClaimModerationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ClaimController {

    private ClaimModerationService claimModerationService;

    public ClaimController(final ClaimModerationService claimModerationService) {
        this.claimModerationService = claimModerationService;
    }

    @GetMapping("/claims")
    public ModelAndView showClaimsPage(final Model model) {
        model.addAttribute("pendingClaims", claimModerationService.listPendingRequestClaims());
        model.addAttribute("failedClaims", claimModerationService.listFailedRequestClaims());
        return new ModelAndView("claims");
    }

    @PostMapping("/claims/approved")
    public ModelAndView approveRequestClaim(@RequestParam("id") Long id) {
        claimModerationService.approveClaim(id);
        return
                new ModelAndView(new RedirectView("/claims", true, true, true));
    }

    @PostMapping("/claims/declined")
    public ModelAndView declineRequestClaim(@RequestParam Long id) {
        claimModerationService.declineClaim(id);
        return
                new ModelAndView(new RedirectView("/claims", true, true, true));
    }
}
