package io.fundrequest.platform.admin.refund;

import io.fundrequest.core.request.fund.dto.RefundRequestDto;
import io.fundrequest.platform.admin.service.ModerationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.ws.rs.BadRequestException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
public class RefundController {

    private final ModerationService<RefundRequestDto> refundModerationService;

    public RefundController(final ModerationService<RefundRequestDto> refundModerationService) {
        this.refundModerationService = refundModerationService;
    }

    @GetMapping("/refunds")
    public ModelAndView getPendingRefunds(final ModelAndView modelAndView) {
        modelAndView.addObject("pendingRefunds", refundModerationService.listPending());
        modelAndView.addObject("failedRefunds", refundModerationService.listFailed());
        modelAndView.setViewName("refunds");
        return modelAndView;
    }


    @RequestMapping(value = "/refunds/{refundRequestId}", method = {POST, PUT})
    public ModelAndView updateRefundRequest(@PathVariable("refundRequestId") final Long refundRequestId,
                                            @RequestParam("action") final String action) {

        switch (action) {
            case "APPROVE":
                refundModerationService.approve(refundRequestId);
                break;
            case "DECLINE":
                refundModerationService.decline(refundRequestId);
                break;
            default:
                throw new BadRequestException("Action not allowed");
        }

        return new ModelAndView(new RedirectView("/refunds", true));
    }
}
