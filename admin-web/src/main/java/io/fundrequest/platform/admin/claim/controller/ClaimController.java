package io.fundrequest.platform.admin.claim.controller;

import io.fundrequest.core.request.claim.dto.RequestClaimDto;
import io.fundrequest.platform.admin.service.ModerationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ClaimController {

    private ModerationService<RequestClaimDto> claimModerationService;

    public ClaimController(final ModerationService<RequestClaimDto> claimModerationService) {
        this.claimModerationService = claimModerationService;
    }

    @GetMapping("/claims")
    public ModelAndView showClaimsPage(final Model model) {
        model.addAttribute("pendingClaims", claimModerationService.listPending());
        model.addAttribute("failedClaims", claimModerationService.listFailed());
        return new ModelAndView("claims");
    }

    @PostMapping("/claims/approved")
    public ModelAndView approveRequestClaim(@RequestParam("id") Long id) {
        claimModerationService.approve(id);
        return new ModelAndView(new RedirectView("/claims", true, true, true));
    }

    @PostMapping("/claims/declined")
    public ModelAndView declineRequestClaim(@RequestParam Long id) {
        claimModerationService.decline(id);
        return new ModelAndView(new RedirectView("/claims", true, true, true));
    }
}
