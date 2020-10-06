package io.fundrequest.core.request.fiat.coinmarketcap.client;

import io.fundrequest.core.request.fiat.coinmarketcap.dto.listing.CmcListingsResult;

public interface CoinMarketCapClient {

    CmcListingsResult getListings();

}
