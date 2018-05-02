package io.fundreqest.platform.tweb.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundreqest.platform.tweb.infrastructure.mav.AbstractController;
import io.fundreqest.platform.tweb.request.dto.ERC67FundDto;
import io.fundreqest.platform.tweb.request.dto.RequestDetailsView;
import io.fundreqest.platform.tweb.request.dto.RequestView;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.ClaimService;
import io.fundrequest.core.request.claim.UserClaimRequest;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.fund.CreateERC67FundRequest;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.PendingFundService;
import io.fundrequest.core.request.fund.dto.PendingFundDto;
import io.fundrequest.core.request.statistics.StatisticsService;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.platform.profile.profile.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Slf4j
public class RequestController extends AbstractController {

    private RequestService requestService;
    private PendingFundService pendingFundService;
    private StatisticsService statisticsService;
    private ProfileService profileService;
    private FundService fundService;
    private ClaimService claimService;
    private ObjectMapper objectMapper;
    private Mappers mappers;

    public RequestController(RequestService requestService,
                             PendingFundService pendingFundService,
                             StatisticsService statisticsService,
                             ProfileService profileService, FundService fundService,
                             ClaimService claimService,
                             ObjectMapper objectMapper,
                             Mappers mappers) {
        this.requestService = requestService;
        this.pendingFundService = pendingFundService;
        this.statisticsService = statisticsService;
        this.profileService = profileService;
        this.fundService = fundService;
        this.claimService = claimService;
        this.objectMapper = objectMapper;
        this.mappers = mappers;
    }

    @GetMapping("/requests")
    public ModelAndView requests() {
        List<RequestView> requests = requestService.findAll().stream().map(this::mapToRequestView).collect(Collectors.toList());
        int noOfFundedRequests = 0;
        int noOfClaimedRequests = 0;
        for (RequestView r : requests) {
            switch (RequestStatus.valueOf(r.getStatus())) {
                case FUNDED:
                    noOfFundedRequests += 1;
                    break;
                case CLAIMED:
                case CLAIM_APPROVED:
                case CLAIM_REQUESTED:
                    noOfClaimedRequests += 1;
                    break;
                default:
            }
        }
        return modelAndView()
                .withObject("requestsFunded", noOfFundedRequests)
                .withObject("requestsClaimed", noOfClaimedRequests)
                .withObject("requests", getAsJson(requests))
                .withObject("statistics", statisticsService.getStatistics())
                .withObject("projects", getAsJson(requestService.findAllProjects()))
                .withObject("technologies", getAsJson(requestService.findAllTechnologies()))
                .withView("pages/requests/index")
                .build();
    }

    @GetMapping("/requests/{id}")
    public ModelAndView details(@PathVariable Long id, Model model) {
        RequestDetailsView request = mappers.map(RequestDto.class, RequestDetailsView.class, requestService.findRequest(id));
        return modelAndView(model)
                .withObject("request", request)
                .withObject("requestJson", getAsJson(request))
                .withObject("fundedBy", fundService.getFundedBy(id))
                .withObject("githubComments", requestService.getComments(id))
                .withView("pages/requests/detail")
                .build();
    }

    @PostMapping("/requests/{id}/claim")
    public ModelAndView claimRequest(Principal principal, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        String etherAddress = profileService.getUserProfile(principal.getName()).getEtherAddress();
        if (StringUtils.isBlank(etherAddress)) {
            return redirectView(redirectAttributes)
                    .withDangerMessage("Please update your profile with a correct ether address.")
                    .url("/requests/" + id)
                    .build();
        }
        RequestDto request = requestService.findRequest(id);
        claimService.claim(principal,
                           UserClaimRequest.builder()
                                           .address(etherAddress)
                                           .platform(request.getIssueInformation().getPlatform())
                                           .platformId(request.getIssueInformation().getPlatformId())
                                           .build());
        return redirectView(redirectAttributes)
                .withSuccessMessage("Your claim has been requested and waiting for approval.")
                .url("/requests/" + id)
                .build();
    }

    @GetMapping("/requests/{id}/actions")
    public ModelAndView detailActions(Principal principal, @PathVariable Long id) {
        RequestDto request = requestService.findRequest(id);
        return modelAndView()
                .withObject("userClaimable", requestService.getUserClaimableResult(principal, id))
                .withObject("request", request)
                .withView("pages/requests/detail-actions")
                .build();
    }

    @GetMapping("/user/requests")
    public ModelAndView userRequests(Principal principal) {
        List<RequestView> requests = requestService.findRequestsForUser(principal).stream()
                                                   .map(this::mapToRequestView)
                                                   .collect(Collectors.toList());

        List<PendingFundDto> pendingFunds = pendingFundService.findByUser(principal);
        return modelAndView()
                .withObject("requests", getAsJson(requests))
                .withObject("pendingFunds", getAsJson(pendingFunds))
                .withView("pages/user/requests")
                .build();
    }

    private RequestView mapToRequestView(RequestDto r) {
        return RequestView
                .builder()
                .id(r.getId())
                .icon("https://github.com/" + r.getIssueInformation().getOwner() + ".png")
                .owner(r.getIssueInformation().getOwner())
                .repo(r.getIssueInformation().getRepo())
                .issueNumber(r.getIssueInformation().getNumber())
                .platform(r.getIssueInformation().getPlatform().name())
                .title(r.getIssueInformation().getTitle())
                .status(r.getStatus().name())
                .starred(r.isLoggedInUserIsWatcher())
                .technologies(r.getTechnologies())
                .funds(r.getFunds())
                .build();
    }

    private String getAsJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
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
