package io.fundreqest.platform.tweb;

import io.fundreqest.platform.tweb.infrastructure.mav.AbstractController;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.ref.RefSignupEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class HomeController extends AbstractController {

    private ProfileService profileService;
    private ApplicationEventPublisher eventPublisher;

    public HomeController(ProfileService profileService, ApplicationEventPublisher eventPublisher) {
        this.profileService = profileService;
        this.eventPublisher = eventPublisher;
    }

    @RequestMapping("/")
    public ModelAndView home(@RequestParam(value = "ref", required = false) String ref, RedirectAttributes redirectAttributes, Principal principal) {
        if (principal != null && StringUtils.isNotBlank(ref)) {
            eventPublisher.publishEvent(RefSignupEvent.builder().principal(principal).ref(ref).build());
            return redirectView(redirectAttributes).url("/").build();
        }
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
