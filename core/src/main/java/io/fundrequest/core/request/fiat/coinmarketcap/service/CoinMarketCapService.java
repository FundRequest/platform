package io.fundrequest.core.request.fiat.coinmarketcap.service;

import io.fundrequest.core.request.fiat.coinmarketcap.client.CoinMarketCapClient;
import io.fundrequest.core.request.fiat.coinmarketcap.dto.listing.CmcListing;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CoinMarketCapService {

    private CoinMarketCapClient client;
    private CacheManager cacheManager;

    public CoinMarketCapService(CoinMarketCapClient client, CacheManager cacheManager) {
        this.client = client;
        this.cacheManager = cacheManager;
    }

    @Cacheable(value = "cmc_token_price", key = "#symbol")
    public Optional<Double> getCurrentPriceInUsd(final String symbol) {
        return getListing(symbol)
                .filter(l -> l.getQuote() != null && l.getQuote().getUsd() != null && l.getQuote().getUsd().getPrice() != null)
                .map(l -> l.getQuote().getUsd().getPrice());
    }

    private Optional<CmcListing> getListing(String symbol) {
        return getListings()
                .stream()
                .filter(l -> symbol.equalsIgnoreCase(l.getSymbol()))
                .findFirst();
    }


    private List<CmcListing> getListings() {
        List listings = cacheManager.getCache("cmc_listings").get("all", List.class);
        if (listings == null) {
            listings = client.getListings().getListings();
            cacheManager.getCache("cmc_listings").put("all", listings);
        }
        return listings;
    }
}
