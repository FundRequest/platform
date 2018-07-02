package io.fundreqest.platform.tweb.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundreqest.platform.tweb.infrastructure.mav.AbstractController;
import io.fundreqest.platform.tweb.request.dto.ERC67FundDto;
import io.fundreqest.platform.tweb.request.dto.RequestDetailsView;
import io.fundreqest.platform.tweb.request.dto.RequestView;
import io.fundrequest.core.infrastructure.SecurityContextService;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.ClaimService;
import io.fundrequest.core.request.claim.UserClaimRequest;
import io.fundrequest.core.request.fiat.FiatService;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.PendingFundService;
import io.fundrequest.core.request.fund.RefundService;
import io.fundrequest.core.request.fund.domain.CreateERC67FundRequest;
import io.fundrequest.core.request.fund.dto.PendingFundDto;
import io.fundrequest.core.request.statistics.StatisticsService;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.platform.profile.profile.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.fundrequest.core.request.domain.Platform.GITHUB;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.APPROVED;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.PENDING;
import static java.util.stream.Collectors.toList;

@Controller
@Slf4j
public class RequestController extends AbstractController {

	private final SecurityContextService securityContextService;
    private final RequestService requestService;
    private final PendingFundService pendingFundService;
    private final StatisticsService statisticsService;
    private final ProfileService profileService;
    private final FundService fundService;
    private final RefundService refundService;
    private final ClaimService claimService;
    private final FiatService fiatService;
    private final ObjectMapper objectMapper;
    private final Mappers mappers;

    public RequestController(final SecurityContextService securityContextService,
							 final RequestService requestService,
                             final PendingFundService pendingFundService,
                             final StatisticsService statisticsService,
                             final ProfileService profileService, FundService fundService,
                             final RefundService refundService,
                             final ClaimService claimService,
                             final FiatService fiatService,
                             final ObjectMapper objectMapper,
                             final Mappers mappers) {
		this.securityContextService = securityContextService;
        this.requestService = requestService;
        this.pendingFundService = pendingFundService;
        this.statisticsService = statisticsService;
        this.profileService = profileService;
        this.fundService = fundService;
        this.refundService = refundService;
        this.claimService = claimService;
        this.fiatService = fiatService;
        this.objectMapper = objectMapper;
        this.mappers = mappers;
    }

    @GetMapping("/requests")
    public ModelAndView requests() {
        final List<RequestView> requests = mappers.mapList(RequestDto.class, RequestView.class, requestService.findAll());
        final Map<String, Long> requestsPerFaseCount = requests.stream().collect(Collectors.groupingBy(RequestView::getFase, Collectors.counting()));
        return modelAndView().withObject("requestsPerFaseCount", requestsPerFaseCount)
                             .withObject("requests", getAsJson(requests))
                             .withObject("statistics", statisticsService.getStatistics())
                             .withObject("projects", getAsJson(requestService.findAllProjects()))
                             .withObject("technologies", getAsJson(requestService.findAllTechnologies()))
                             .withObject("isAuthenticated", getAsJson(securityContextService.isUserFullyAuthenticated()))
                             .withView("pages/requests/index")
                             .build();
    }

    @RequestMapping("/requests/{type}")
    public ModelAndView details(@PathVariable String type, @RequestParam Map<String, String> queryParameters) {
        return modelAndView()
                .withObject("url", queryParameters.get("url"))
                .withView("pages/fund/" + type)
                .build();
    }

    @GetMapping("/requests/{id}")
    public ModelAndView details(@PathVariable Long id, Model model) {
        final RequestDetailsView request = mappers.map(RequestDto.class, RequestDetailsView.class, requestService.findRequest(id));
        return getDetailsModelAndView(id, model, request);
    }

    @GetMapping("/requests/github/{owner}/{repo}/{number}")
    public ModelAndView details(@PathVariable String owner, @PathVariable String repo, @PathVariable String number, Model model) {
        final String platformId = owner + "|FR|" + repo + "|FR|" + number;
        final RequestDetailsView request = mappers.map(RequestDto.class, RequestDetailsView.class, requestService.findRequest(GITHUB, platformId));
        return getDetailsModelAndView(request.getId(), model, request);
    }

    private ModelAndView getDetailsModelAndView(final Long id, final Model model, final RequestDetailsView request) {
        return modelAndView(model)
                .withObject("request", request)
                .withObject("requestJson", getAsJson(request))
                .withObject("funds", fundService.getFundsForRequestGroupedByFunder(id))
                .withObject("claims", claimService.getAggregatedClaimsForRequest(id))
                .withObject("pendingRefundAddresses", refundService.findAllRefundRequestsFor(id, PENDING, APPROVED)
                                                                   .stream()
                                                                   .map(refundRequest -> refundRequest.getFunderAddress().toLowerCase())
                                                                   .collect(toList()))
                .withObject("githubComments", requestService.getComments(id))
                .withView("pages/requests/detail")
                .build();
    }

    @GetMapping(value = "/requests/{id}/badge", produces = "image/svg+xml")
    public ModelAndView detailsBadge(@PathVariable final Long id, final Model model, final HttpServletResponse response) {
        final RequestDto request = requestService.findRequest(id);
        response.setHeader(HttpHeaders.CACHE_CONTROL, CacheControl.noStore().getHeaderValue());

        final double fndUsdPrice = fiatService.getUsdPrice(request.getFunds().getFndFunds());
        final double otherFundsUsdPrice = fiatService.getUsdPrice(request.getFunds().getOtherFunds());

        return modelAndView(model)
                .withObject("requestFase", request.getStatus().getFase())
                .withObject("highestFunds", fndUsdPrice >= otherFundsUsdPrice ? request.getFunds().getFndFunds() : request.getFunds().getOtherFunds())
                .withView("requests/badge.svg")
                .build();
    }

    @PostMapping("/requests/{id}/claim")
    public ModelAndView claimRequest(Principal principal, @PathVariable Long id, RedirectAttributes redirectAttributes) {
        String etherAddress = profileService.getUserProfile(principal.getName()).getEtherAddress();
        if (StringUtils.isBlank(etherAddress)) {
            return redirectView(redirectAttributes)
                    .withDangerMessage("Please update <a href=\"/profile\">your profile</a> with a correct ether address.")
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
                .withSuccessMessage("Your claim has been requested and is waiting for approval.")
                .url("/requests/" + id)
                .build();
    }

    @GetMapping("/requests/{id}/actions")
    public ModelAndView detailActions(Principal principal, @PathVariable Long id) {
        return modelAndView()
                .withObject("userClaimable", requestService.getUserClaimableResult(principal, id))
                .withObject("request", requestService.findRequest(id))
                .withView("pages/requests/detail-actions :: details")
                .build();
    }

    @PostMapping(value = {"/requests/{id}/watch"}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String toggleWatchRequest(Principal principal, @PathVariable Long id) {
        RequestDto request = requestService.findRequest(id);
        if (request.isLoggedInUserIsWatcher()) {
            request.setLoggedInUserIsWatcher(false);
            requestService.removeWatcherFromRequest(principal, id);
        } else {
            request.setLoggedInUserIsWatcher(true);
            requestService.addWatcherToRequest(principal, id);
        }
        RequestView requestview = mappers.map(RequestDto.class, RequestView.class, request);

        return getAsJson(requestview);
    }

    @GetMapping("/user/requests")
    public ModelAndView userRequests(Principal principal) {
        final List<RequestView> requests = mappers.mapList(RequestDto.class, RequestView.class, requestService.findRequestsForUser(principal));
        final List<PendingFundDto> pendingFunds = pendingFundService.findByUser(principal);
        return modelAndView()
                .withObject("requests", getAsJson(requests))
                .withObject("pendingFunds", getAsJson(pendingFunds))
                .withObject("isAuthenticated", getAsJson(securityContextService.isUserFullyAuthenticated()))
                .withView("pages/user/requests")
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
