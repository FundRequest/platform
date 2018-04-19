package io.fundrequest.core.request.fiat;

import io.fundrequest.core.request.fiat.cryptocompare.service.CryptoCompareService;
import io.fundrequest.core.request.fund.dto.TotalFundDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;

@Service
public class FiatService {

    private CryptoCompareService cryptoCompareService;

    public FiatService(CryptoCompareService cryptoCompareService) {
        this.cryptoCompareService = cryptoCompareService;
    }

    public double getUsdPrice(TotalFundDto... funds) {
        return Arrays.stream(funds)
                     .filter(f -> f != null && StringUtils.isNotBlank(f.getTokenSymbol()))
                     .map(f -> cryptoCompareService.getCurrentPriceInUsd(f.getTokenSymbol()))
                     .filter(Objects::nonNull)
                     .mapToDouble(f -> f)
                     .sum();
    }
}
