package io.fundrequest.web.request;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.web.request.dto.ChartDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Controller
public class RequestsController {

    private RequestService requestService;

    @Autowired
    public RequestsController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/requests")
    public String showRequests(String name, @AuthenticationPrincipal Authentication authentication, Model model) {
        model.addAttribute("requests", requestService.findAll());
        return "requests";
    }

    @GetMapping("/requests/add")
    public String showAddRequestFragment(Model model) {
        model.addAttribute("createRequestCommand", new CreateRequestCommand());
        return "fragments/addRequestFragment";
    }

    @GetMapping("/requests/fund/{id}")
    public String showFundRequestFragment(Model model, @PathVariable("id") Long id) {
        model.addAttribute("request", requestService.findRequest(id));
        return "fragments/fundRequestFragment";
    }

    @GetMapping("/requests/{id}")
    public String getOne(Model model, @PathVariable("id") Long id) {
        model.addAttribute("request", requestService.findRequest(id));
        return "request";
    }

    @GetMapping("/requests/{id}/watchlink")
    public String getWatchLink(Model model, @PathVariable("id") Long id) {
        getOne(model, id);
        return "fragments/watchLink :: watchLink";
    }

    @GetMapping("/requests/{id}/watchers")
    public String getWatchers(Model model, @PathVariable("id") Long id) {
        getOne(model, id);
        return "request :: requestWatchers";
    }

    @PostMapping("/requests")
    public ModelAndView addRequest(@Valid CreateRequestCommand createRequestCommand, BindingResult bindingResult, @AuthenticationPrincipal Authentication webUser) {
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

    @PostMapping("/requests/{id}/watchers")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateWatchingStatus(@PathVariable("id") Long id,
                                     @RequestParam(value = "watch") Boolean watch,
                                     @AuthenticationPrincipal Authentication webUser) {
        if (watch) {
            requestService.addWatcherToRequest(webUser, id);
        } else {
            requestService.removeWatcherFromRequest(webUser, id);
        }
    }

    @GetMapping("/requests/charts/barchart-status")
    @ResponseBody
    public Map<String, String> getChartData() {
        return requestService.findAll()
                .stream()
                .collect(Collectors.groupingBy(r -> r.getStatus().toString(), Collectors.counting()))
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, r -> r.getValue().toString()));

    }
}
