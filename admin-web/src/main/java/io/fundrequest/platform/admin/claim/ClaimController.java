package io.fundrequest.platform.admin.claim;

import io.fundrequest.core.request.claim.ClaimService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ClaimController {

    private ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping("/claims")
    public ModelAndView showClaimsPage(Model model) {
        model.addAttribute("pendingClaims", claimService.listPendingRequestClaims());
        return new ModelAndView("claims");
    }

    @PostMapping("/claims/approved")
    public ModelAndView approveRequestClaim(@RequestParam("id") Long id) {
        claimService.approveClaim(id);
        return
                new ModelAndView(new RedirectView("/claims", true, true, true));
    }

    @PostMapping("/claims/declined")
    public ModelAndView declineRequestClaim(@RequestParam Long id) {
        claimService.declineClaim(id);
        return
                new ModelAndView(new RedirectView("/claims", true, true, true));
    }

}
