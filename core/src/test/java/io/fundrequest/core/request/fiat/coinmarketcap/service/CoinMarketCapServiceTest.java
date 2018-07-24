package io.fundrequest.core.request.fiat.coinmarketcap.service;

import io.fundrequest.core.request.fiat.coinmarketcap.client.CoinMarketCapClient;
import io.fundrequest.core.request.fiat.coinmarketcap.dto.listing.CmcListing;
import io.fundrequest.core.request.fiat.coinmarketcap.dto.listing.CmcListingsResult;
import io.fundrequest.core.request.fiat.coinmarketcap.dto.ticker.CmcTickerData;
import io.fundrequest.core.request.fiat.coinmarketcap.dto.ticker.CmcTickerQuote;
import io.fundrequest.core.request.fiat.coinmarketcap.dto.ticker.CmcTickerQuotes;
import io.fundrequest.core.request.fiat.coinmarketcap.dto.ticker.CmcTickerResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CoinMarketCapServiceTest {

    private CoinMarketCapService service;
    private CoinMarketCapClient client;
    private CacheManager cacheManager;

    @BeforeEach
    void setUp() {
        client = mock(CoinMarketCapClient.class);
        cacheManager = mock(CacheManager.class, RETURNS_DEEP_STUBS);
        service = new CoinMarketCapService(client, cacheManager);
        when(cacheManager.getCache("cmc_listings").get("all", List.class)).thenReturn(null);
    }

    @Test
    void getPrice() {
        CmcListingsResult listingResult = new CmcListingsResult();
        listingResult.setListings(Collections.singletonList(CmcListing.builder().id(2751L).symbol("FND").build()));
        when(client.getListings()).thenReturn(listingResult);
        when(client.getTickerById(2751L)).thenReturn(CmcTickerResult.builder()
                                                                    .data(
                                                                            CmcTickerData.builder()
                                                                                         .id(2751L)
                                                                                         .symbol("FND")
                                                                                         .quotes(
                                                                                                 CmcTickerQuotes.builder()
                                                                                                                .usd(
                                                                                                                        CmcTickerQuote.builder()
                                                                                                                                      .price(1.1)
                                                                                                                                      .build())
                                                                                                                .build())
                                                                                         .build())
                                                                    .build()
                                                    );

        Optional<Double> result = service.getCurrentPriceInUsd("FND");

        assertThat(result).hasValue(1.1);
    }

    @Test
    void getPriceEmpty() {
        CmcListingsResult listingResult = new CmcListingsResult();
        listingResult.setListings(new ArrayList<>());
        when(client.getListings()).thenReturn(listingResult);

        Optional<Double> result = service.getCurrentPriceInUsd("FND");

        assertThat(result).isEmpty();
    }
}