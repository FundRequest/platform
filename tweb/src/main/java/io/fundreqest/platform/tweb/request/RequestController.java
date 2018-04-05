package io.fundreqest.platform.tweb.request;

import io.fundreqest.platform.tweb.infrastructure.mav.AbstractController;
import io.fundrequest.core.request.RequestService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class RequestController extends AbstractController {

    private RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @RequestMapping("/requests")
    public ModelAndView requests() {
        requestService.findAll();
        return modelAndView()
                .withObject("requests", requestService.findAll())
                .withView("pages/requests/index")
                .build();
    }

    @RequestMapping("/requests/{id}")
    public ModelAndView details(@PathVariable Long id) {
        return modelAndView()
                .withObject("request", requestService.findRequest(id))
                .withView("pages/requests/detail")
                .build();
    }


}
