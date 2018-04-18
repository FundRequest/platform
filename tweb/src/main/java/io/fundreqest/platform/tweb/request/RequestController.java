package io.fundreqest.platform.tweb.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundreqest.platform.tweb.infrastructure.mav.AbstractController;
import io.fundreqest.platform.tweb.request.dto.ERC67FundDto;
import io.fundreqest.platform.tweb.request.dto.RequestView;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.fund.CreateERC67FundRequest;
import lombok.extern.slf4j.Slf4j;
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
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class RequestController extends AbstractController {

    private RequestService requestService;
    private ObjectMapper objectMapper;

    public RequestController(RequestService requestService, ObjectMapper objectMapper) {
        this.requestService = requestService;
        this.objectMapper = objectMapper;
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
        List<RequestView> requests = requestService.findRequestsForUser(principal).stream()
                                                   .map(r -> RequestView
                                                           .builder()
                                                           .icon("https://github.com/" + r.getIssueInformation().getOwner() + ".png")
                                                           .owner(r.getIssueInformation().getOwner())
                                                           .repo(r.getIssueInformation().getRepo())
                                                           .issueNumber(r.getIssueInformation().getNumber())
                                                           .platform(r.getIssueInformation().getPlatform().name())
                                                           .title(r.getIssueInformation().getTitle())
                                                           .status(r.getStatus().name())
                                                           .starred(r.isLoggedInUserIsWatcher())
                                                           .technologies(r.getTechnologies())
                                                           .fndFunds(r.getFndFunds())
                                                           .otherFunds(r.getOtherFunds())
                                                           .build()
                                                       )
                                                   .collect(Collectors.toList());
        return modelAndView()
                .withObject("requests", getAsJson(requests))
                .withView("pages/user/requests")
                .build();
    }

    private String getAsJson(List<RequestView> requests) {
        try {
            return objectMapper.writeValueAsString(requests);
        } catch (JsonProcessingException e) {
            log.error("Error creating json", e);
            return "";
        }
    }

    @PostMapping(value = {"/rest/requests/erc67/fund"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ERC67FundDto generateERC67ForFunding(@RequestBody @Valid CreateERC67FundRequest createERC67FundRequest) {
        return ERC67FundDto.builder().erc67Link(requestService.generateERC67(createERC67FundRequest)).build();
    }


}
