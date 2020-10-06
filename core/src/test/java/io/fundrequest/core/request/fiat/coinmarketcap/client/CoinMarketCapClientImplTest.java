package io.fundrequest.core.request.fiat.coinmarketcap.client;

import io.fundrequest.core.request.fiat.coinmarketcap.dto.listing.CmcListingsResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Disabled
class CoinMarketCapClientImplTest {

    private CoinMarketCapClientImpl client;

    @BeforeEach
    void setUp() {
        client = new CoinMarketCapClientImpl("the_api_key");
    }

    @Test
    void getListings() {
        CmcListingsResult listings = client.getListings();

        assertThat(listings).isNotNull();
    }
}
