package io.fundreqest.platform.tweb;

import io.fundrequest.core.contract.service.FundRequestContractsService;
import io.fundrequest.platform.profile.profile.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class HomeController {

    private FundRequestContractsService contractsService;
    private ProfileService profileService;

    public HomeController(FundRequestContractsService contractsService, ProfileService profileService) {
        this.contractsService = contractsService;
        this.profileService = profileService;
    }

    @RequestMapping("/")
    public ModelAndView home() {
        contractsService.fundRepository().getFundedTokenCount("GITHUB", "FundRequest|FR|area51|FR|38");

        return new ModelAndView("index");
    }

    @GetMapping(path = "/logout")
    public String logout(Principal principal, HttpServletRequest request) throws ServletException {
        if (principal != null) {
            profileService.logout(principal);
        }
        request.logout();
        return "redirect:/";
    }
}
