package io.fundreqest.platform.tweb.request;

import io.fundreqest.platform.tweb.infrastructure.mav.AbstractController;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.fund.CreateERC67FundRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class RequestController extends AbstractController {

    private RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @RequestMapping("/requests")
    public ModelAndView requests() {
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

    @PostMapping(value = {"/rest/requests/{id}/erc67/fund"}, produces = MediaType.TEXT_PLAIN_VALUE)
    public String generateERC67ForFunding(@RequestBody @Valid CreateERC67FundRequest createERC67FundRequest) {
        return requestService.generateERC67(createERC67FundRequest);
    }


}
