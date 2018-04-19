package io.fundrequest.core.request.view;

import io.fundrequest.core.request.fiat.FiatService;
import io.fundrequest.core.request.fund.domain.PendingFund;
import io.fundrequest.core.request.fund.dto.PendingFundDto;
import io.fundrequest.core.request.fund.dto.TotalFundDto;
import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class PendingFundDtoMapperDecorator implements PendingFundDtoMapper {

    @Autowired
    @Qualifier("delegate")
    private PendingFundDtoMapper delegate;

    @Autowired
    private FiatService fiatService;

    @Autowired
    private TokenInfoService tokenInfoService;

    public PendingFundDto map(PendingFund fund) {
        PendingFundDto result = delegate.map(fund);
        if (result != null && result.getAmount() != null) {
            mapFunds(getTotalFundDto(fund), result);
        }

        return result;
    }

    private TotalFundDto getTotalFundDto(PendingFund fund) {
        final TokenInfoDto tokenInfo = tokenInfoService.getTokenInfo(fund.getTokenAddress());
        final BigDecimal divider = BigDecimal.valueOf(10).pow(tokenInfo.getDecimals());
        return TotalFundDto.builder()
                           .tokenAddress(tokenInfo.getAddress())
                           .tokenSymbol(tokenInfo.getSymbol())
                           .totalAmount(new BigDecimal(fund.getAmount()).divide(divider, 6, RoundingMode.HALF_DOWN))
                           .build();
    }

    private void mapFunds(TotalFundDto totalFund, PendingFundDto result) {
        AllFundsDto funds = result.getFunds();
        if ("FND".equalsIgnoreCase(totalFund.getTokenSymbol())) {
            funds.setFndFunds(totalFund);
        } else {
            funds.setOtherFunds(totalFund);
        }
        funds.setUsdFunds(fiatService.getUsdPrice(funds.getFndFunds(), funds.getOtherFunds()));
    }

}
