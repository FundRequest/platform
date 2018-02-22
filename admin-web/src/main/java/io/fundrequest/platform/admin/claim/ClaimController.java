package io.fundrequest.platform.admin.claim;

import io.fundrequest.core.request.claim.ClaimService;
import io.fundrequest.core.request.claim.dto.RequestClaimDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ClaimController {

    private ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @GetMapping("/claims")
    public ModelAndView listClaims(Model model) {
        List<RequestClaimDto> claims = claimService.listRequestClaims();
        model.addAttribute("pendingClaims", claims);
        return new ModelAndView("claims");
    }

}
