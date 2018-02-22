package io.fundrequest.platform.admin.claim;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ClaimController {
    @GetMapping("/claims")
    public ModelAndView listClaims() {
        return new ModelAndView("claims");
    }

}
