package io.fundrequest.platform.tweb.fund;

import io.fundrequest.common.infrastructure.mav.AbstractController;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.view.RequestDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FundController extends AbstractController {

    private final RequestService requestService;

    public FundController(final RequestService requestService) {
        this.requestService = requestService;
    }

    @RequestMapping("/fund/{type}")
    public ModelAndView details(@PathVariable String type, @RequestParam(name = "url", required = false) String url) {
        return modelAndView()
                .withObject("url", url)
                .withView("pages/fund/" + type)
                .build();
    }

    @RequestMapping("/requests/{request-id}/fund")
    public ModelAndView fundRequestById(@PathVariable("request-id") Long requestId) {
        final RequestDto request = requestService.findRequest(requestId);
        return modelAndView()
                .withObject("url", request.getIssueInformation().getUrl())
                .withView("pages/fund/" + request.getIssueInformation().getPlatform().name().toLowerCase())
                .build();
    }
}
