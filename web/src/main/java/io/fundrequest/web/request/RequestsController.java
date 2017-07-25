package io.fundrequest.web.request;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.web.security.WebUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class RequestsController {

    private RequestService requestService;

    @Autowired
    public RequestsController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/requests")
    public String showRequests(String name, @AuthenticationPrincipal WebUser webUser, Model model) {
        model.addAttribute("requests", requestService.findAll());
        return "requests";
    }

    @GetMapping("/requests/add")
    public String showAddRequestFragment(Model model) {
        model.addAttribute("createRequestCommand", new CreateRequestCommand());
        return "fragments/addRequestFragment";
    }

    @PostMapping("/requests")
    public ModelAndView addRequest(@Valid CreateRequestCommand createRequestCommand, BindingResult bindingResult, @AuthenticationPrincipal WebUser webUser) {
        if (bindingResult.hasErrors()) {
            ModelAndView mav = new ModelAndView("fragments/addRequestFragment");
            mav.addObject("createRequestCommand", createRequestCommand);
            return mav;
        }
        requestService.createRequest(webUser, createRequestCommand);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setStatus(HttpStatus.NO_CONTENT);
        return modelAndView;
    }
}
