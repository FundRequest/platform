package io.fundreqest.platform.tweb.fund;

import io.fundrequest.core.contract.service.FundRequestContractsService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest")
public class FundRestController {

    private FundRequestContractsService fundRequestContractsService;

    public FundRestController(final FundRequestContractsService fundRequestContractsService) {
        this.fundRequestContractsService = fundRequestContractsService;
    }

    @RequestMapping("/fund/allowed-tokens")
    public List<TokenInfoDto> getAllowedTokens(@RequestParam("platform") final String platform,
                                               @RequestParam("platformId") final String platformId) {
        return fundRequestContractsService.getAllPossibleTokens(platform, platformId);
    }
}
