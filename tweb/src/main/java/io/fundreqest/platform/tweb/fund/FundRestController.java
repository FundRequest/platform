package io.fundreqest.platform.tweb.fund;

import io.fundrequest.core.contract.service.FundRequestContractsService;
import io.fundrequest.core.request.fund.RefundService;
import io.fundrequest.core.request.fund.command.RequestRefundCommand;
import io.fundrequest.core.token.dto.TokenInfoDto;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class FundRestController {

    private FundRequestContractsService fundRequestContractsService;
    private final RefundService refundService;

    public FundRestController(final FundRequestContractsService fundRequestContractsService, final RefundService refundService) {
        this.fundRequestContractsService = fundRequestContractsService;
        this.refundService = refundService;
    }

    @RequestMapping("/fund/allowed-tokens")
    public List<TokenInfoDto> getAllowedTokens(@RequestParam("platform") final String platform,
                                               @RequestParam("platformId") final String platformId) {
        return fundRequestContractsService.getAllPossibleTokens(platform, platformId);
    }

    @PostMapping(value = "/requests/{requestId}/refunds", consumes = "application/json")
    @ResponseBody
    public void requestRefund(@PathVariable("requestId") final Long requestId,
                              @RequestParam("funderAddress") final String funderAddress,
                              @RequestParam("r") final String r,
                              @RequestParam("v") final String v,
                              @RequestParam("s") final String s) {

        refundService.requestRefund(RequestRefundCommand.builder().requestId(requestId)
                                                        .funderAddress(funderAddress)
                                                        .r(r)
                                                        .v(v)
                                                        .s(s)
                                                        .build());
    }
}
