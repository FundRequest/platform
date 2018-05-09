package io.fundreqest.platform.tweb;

import io.fundreqest.platform.tweb.infrastructure.mav.AbstractController;
import io.fundrequest.platform.profile.profile.ProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class HomeController extends AbstractController {

    private ProfileService profileService;

    public HomeController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @RequestMapping("/")
    public ModelAndView home() {
        return new ModelAndView("index");
    }

    @RequestMapping("/user/login")
    public ModelAndView login(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        return redirectView(redirectAttributes)
                .url(request.getHeader("referer"))
                .build();
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
