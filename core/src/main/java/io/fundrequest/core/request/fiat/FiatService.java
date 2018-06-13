package io.fundrequest.core.request.fiat;

import io.fundrequest.core.request.fiat.cryptocompare.service.CryptoCompareService;
import io.fundrequest.core.token.dto.TokenValueDto;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Objects;

@Service
public class FiatService {

    private CryptoCompareService cryptoCompareService;

    public FiatService(CryptoCompareService cryptoCompareService) {
        this.cryptoCompareService = cryptoCompareService;
    }

    public double getUsdPrice(TokenValueDto... funds) {
        return Arrays.stream(funds)
                     .filter(f -> f != null && StringUtils.isNotBlank(f.getTokenSymbol()))
                     .map(this::getValue)
                     .filter(Objects::nonNull)
                     .mapToDouble(f -> f)
                     .sum();
    }

    private Double getValue(TokenValueDto f) {
        Double tokenPrice = cryptoCompareService.getCurrentPriceInUsd(f.getTokenSymbol());
        if (tokenPrice != null) {
            return f.getTotalAmount().multiply(BigDecimal.valueOf(tokenPrice)).doubleValue();
        }
        return null;
    }
}
