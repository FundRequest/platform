package io.fundreqest.platform.tweb.fund;

import io.fundreqest.platform.tweb.infrastructure.mav.AbstractController;
import io.fundreqest.platform.tweb.infrastructure.mav.builder.RedirectBuilder;
import io.fundrequest.core.infrastructure.exception.ResourceNotFoundException;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.fund.RefundService;
import io.fundrequest.core.request.fund.command.RequestRefundCommand;
import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Optional;

import static io.fundrequest.core.request.domain.RequestStatus.FUNDED;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.APPROVED;
import static io.fundrequest.core.request.fund.domain.RefundRequestStatus.PENDING;

@Controller
public class FundController extends AbstractController {

    private final RefundService refundService;
    private final ProfileService profileService;
    private final RequestService requestService;

    public FundController(final RequestService requestService,
                          final RefundService refundService,
                          final ProfileService profileService) {
        this.requestService = requestService;
        this.refundService = refundService;
        this.profileService = profileService;
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

    @PostMapping(value = "/requests/{requestId}/refunds")
    public ModelAndView requestRefund(final Principal principal,
                                      @PathVariable("requestId") final Long requestId,
                                      @RequestParam("funder_address") final String funderAddress,
                                      final RedirectAttributes redirectAttributes) {
        final RedirectBuilder redirectBuilder = redirectView(redirectAttributes).url("/requests/" + requestId + "#details");
        if (isValid(redirectBuilder, principal, requestId, funderAddress)) {
            refundService.requestRefund(RequestRefundCommand.builder()
                                                            .requestId(requestId)
                                                            .funderAddress(funderAddress)
                                                            .requestedBy(principal.getName())
                                                            .build());
            return redirectBuilder.withSuccessMessage("Your refund has been requested and is waiting for approval.").build();
        } else {
            return redirectBuilder.build();
        }
    }

    private boolean isValid(final RedirectBuilder redirectBuilder, final Principal principal, final Long requestId, final String funderAddress) {
        boolean errors = false;
        final UserProfile userProfile = profileService.getUserProfile(principal);
        if (!userProfile.isEtherAddressVerified()) {
            redirectBuilder.withDangerMessage("You need to validate your ETH address before you can request refunds. You can do this on your <a href='/profile'>profile</a> page.");
            errors = true;
        }
        if (!StringUtils.equalsIgnoreCase(funderAddress, userProfile.getEtherAddress())) {
            redirectBuilder.withDangerMessage("Your request for a refund is not allowed because the address used to fund does not match the address on your profile.");
            errors = true;
        }

        final RequestDto request = Optional.ofNullable(requestService.findRequest(requestId)).orElseThrow(ResourceNotFoundException::new);
        if (FUNDED != request.getStatus()) {
            redirectBuilder.withDangerMessage("The status of the request needs to be 'Funded' to be able to ask a refund");
            errors = true;
        }

        if (refundRequestAlreadyExists(requestId, funderAddress)) {
            redirectBuilder.withDangerMessage("A refund for this issue and address has already been requested");
            errors = true;
        }

        return !errors;
    }

    private boolean refundRequestAlreadyExists(final long requestId, final String userAddress) {
        return refundService.findAllRefundRequestsFor(requestId, PENDING, APPROVED)
                            .stream()
                            .map(RefundRequestDto::getFunderAddress)
                            .anyMatch(userAddress::equalsIgnoreCase);
    }
}
