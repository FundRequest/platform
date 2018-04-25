package io.fundrequest.core.request.fiat.cryptocompare.service;

import io.fundrequest.core.request.fiat.cryptocompare.client.CryptoCompareClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CryptoCompareService {

    private final CryptoCompareClient client;

    public CryptoCompareService(CryptoCompareClient client) {
        this.client = client;
    }

    @Cacheable(value = "token_price", key = "#symbol")
    public Double getCurrentPriceInUsd(final String symbol) {
        String price = client.getPrice(symbol).getUSD();
        return price == null ? null : Double.valueOf(price);
    }
}
