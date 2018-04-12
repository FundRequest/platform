package io.fundreqest.platform.tweb.profile.mastery;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
public class MasteryController {

    @RequestMapping("/profile/mastery")
    public ModelAndView rewards(Principal principal) {
        ModelAndView mav = new ModelAndView("pages/profile/mastery");
        return mav;
    }
}
