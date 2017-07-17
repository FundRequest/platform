package io.fundrequest.web.request;

import io.fundrequest.core.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RequestsController {

    private RequestService requestService;

    @Autowired
    public RequestsController(RequestService requestService) {
        this.requestService = requestService;
    }

    @RequestMapping("/requests")
    public String showRequests(String name, Model model) {
        model.addAttribute("requests", requestService.findAll());
        return "requests";
    }
}
