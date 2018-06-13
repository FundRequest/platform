package io.fundreqest.platform.tweb.fund;

import io.fundreqest.platform.tweb.infrastructure.mav.AbstractController;
import io.fundreqest.platform.tweb.infrastructure.mav.builder.RedirectBuilder;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.fund.RefundService;
import io.fundrequest.core.request.fund.command.RequestRefundCommand;
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
        final RedirectBuilder redirectBuilder = redirectView(redirectAttributes).url("/requests/" + requestId + "#funded-by");
        if (isValid(redirectBuilder, principal, funderAddress)) {
            refundService.requestRefund(RequestRefundCommand.builder().requestId(requestId).funderAddress(funderAddress).build());
            return redirectBuilder.withSuccessMessage("Your refund has been requested and is waiting for approval.").build();
        } else {
            return redirectBuilder.build();
        }
    }

    private boolean isValid(final RedirectBuilder redirectBuilder, final Principal principal, final String funderAddress) {
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
        return !errors;
    }
}
