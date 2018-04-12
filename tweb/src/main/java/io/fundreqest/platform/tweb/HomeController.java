package io.fundreqest.platform.tweb;

import io.fundrequest.core.contract.service.FundRequestContractsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @Autowired
    private FundRequestContractsService contractsService;

    @RequestMapping("/")
    public ModelAndView home() {
        contractsService.fundRepository().getFundedTokenCount("GITHUB", "FundRequest|FR|area51|FR|38");

        return new ModelAndView("index");
    }

    @GetMapping(path = "/logout")
    public String logout(HttpServletRequest request) throws ServletException {
        request.logout();
        return "redirect:/";
    }
}
