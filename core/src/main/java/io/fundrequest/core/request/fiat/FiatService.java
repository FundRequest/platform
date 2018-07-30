package io.fundrequest.core.request.fiat;

import io.fundrequest.common.infrastructure.exception.ResourceNotFoundException;
import io.fundrequest.core.request.fiat.coinmarketcap.service.CoinMarketCapService;
import io.fundrequest.core.request.fiat.cryptocompare.service.CryptoCompareService;
import io.fundrequest.core.token.dto.TokenValueDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class FiatService {

    private CryptoCompareService cryptoCompareService;

    private CoinMarketCapService coinMarketCapService;

    public FiatService(CryptoCompareService cryptoCompareService, CoinMarketCapService coinMarketCapService) {
        this.cryptoCompareService = cryptoCompareService;
        this.coinMarketCapService = coinMarketCapService;
    }

    public double getUsdPrice(TokenValueDto... funds) {
        return Arrays.stream(funds)
                     .filter(f -> f != null && StringUtils.isNotBlank(f.getTokenSymbol()))
                     .map(this::getValue)
                     .mapToDouble(f -> f)
                     .sum();
    }

    private Double getValue(TokenValueDto f) {
        Double currentPrice = coinMarketCapService.getCurrentPriceInUsd(f.getTokenSymbol())
                                                  .orElseGet(() -> cryptoCompareService.getCurrentPriceInUsd(f.getTokenSymbol())
                                                                                       .orElseThrow(ResourceNotFoundException::new));
        return calculateResult(f, currentPrice);

    }

    private Double calculateResult(TokenValueDto tokenvalue, Double currentPrice) {
        return tokenvalue.getTotalAmount().multiply(BigDecimal.valueOf(currentPrice)).doubleValue();
    }
}
