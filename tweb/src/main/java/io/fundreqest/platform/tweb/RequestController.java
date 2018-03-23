package io.fundreqest.platform.tweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RequestController {

    @RequestMapping("/requests")
    public ModelAndView requests() {
        return new ModelAndView("pages/requests/index");
    }

    @RequestMapping("/requests/{id}")
    public ModelAndView details() {
        return new ModelAndView("pages/requests/detail");
    }
}
