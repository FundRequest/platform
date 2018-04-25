package io.fundrequest.core.request.fiat.cryptocompare.client;

import io.fundrequest.core.request.fiat.cryptocompare.dto.PriceResultDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        name = "cryptocompare-client",
        url = "https://min-api.cryptocompare.com"
)
public interface CryptoCompareClient {

    @RequestMapping(value = "/data/price?fsym={symbol}&tsyms=USD", method = RequestMethod.GET)
    PriceResultDto getPrice(final @PathVariable("symbol") String symbol);

}
