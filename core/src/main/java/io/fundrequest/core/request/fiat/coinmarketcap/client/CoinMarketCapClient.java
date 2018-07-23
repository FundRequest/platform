package io.fundrequest.core.request.fiat.coinmarketcap.client;

import io.fundrequest.core.request.fiat.coinmarketcap.dto.listing.CmcListingsResult;
import io.fundrequest.core.request.fiat.coinmarketcap.dto.ticker.CmcTickerResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        name = "coinmarketcap-client",
        url = "https://api.coinmarketcap.com/v2"
)
public interface CoinMarketCapClient {


    @RequestMapping(value = "/listings", method = RequestMethod.GET)
    CmcListingsResult getListings();

    @RequestMapping(value = "/ticker/{id}?convert=USD", method = RequestMethod.GET)
    CmcTickerResult getTickerById(@PathVariable("id") final Long id);
}