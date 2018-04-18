package io.fundreqest.platform.tweb.request;

import io.fundreqest.platform.tweb.infrastructure.mav.AbstractController;
import io.fundreqest.platform.tweb.request.dto.ERC67FundDto;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.fund.CreateERC67FundRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class RequestController extends AbstractController {

    private RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/requests")
    public ModelAndView requests() {
        return modelAndView()
                .withObject("requests", requestService.findAll())
                .withView("pages/requests/index")
                .build();
    }

    @GetMapping("/requests/{id}")
    public ModelAndView details(@PathVariable Long id) {
        return modelAndView()
                .withObject("request", requestService.findRequest(id))
                .withView("pages/requests/detail")
                .build();
    }

    @GetMapping("/user/requests")
    public ModelAndView details(Principal principal) {
        return modelAndView()
                .withObject("request", requestService.findRequestsForUser(principal))
                .withView("pages/user/requests")
                .build();
    }

    @PostMapping(value = {"/rest/requests/erc67/fund"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ERC67FundDto generateERC67ForFunding(@RequestBody @Valid CreateERC67FundRequest createERC67FundRequest) {
        return ERC67FundDto.builder().erc67Link(requestService.generateERC67(createERC67FundRequest)).build();
    }


}
