package io.fundrequest.web.request;

import io.fundrequest.core.request.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private RequestService requestService;

    @Autowired
    public HomeController(RequestService requestService) {
        this.requestService = requestService;
    }

    @RequestMapping("/")
    public String showRequests(Model model) {
        return "home";
    }
}
