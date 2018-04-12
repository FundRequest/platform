package io.fundreqest.platform.tweb.fund;

import io.fundreqest.platform.tweb.infrastructure.mav.AbstractController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FundController extends AbstractController {

    public FundController() {
    }

    @RequestMapping("/fund")
    public ModelAndView requests() {
        return modelAndView()
                .withView("pages/fund/index")
                .build();
    }

    @RequestMapping("/fund/{type}")
    public ModelAndView details(@PathVariable String type) {
        return modelAndView()
                .withView("pages/fund/" + type)
                .build();
    }


}
