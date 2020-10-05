package io.fundrequest.core.request.fiat.coinmarketcap.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.infrastructure.resttemplate.RestTemplateUserAgentInterceptor;
import io.fundrequest.core.request.fiat.coinmarketcap.dto.listing.CmcListingsResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CoinMarketCapClientImpl implements CoinMarketCapClient {

    private final RestTemplate restTemplate;
    private static final String endpoint = "https://pro-api.coinmarketcap.com";

    public CoinMarketCapClientImpl(@Value("${coinmarketcap.apikey") String apiKey) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        restTemplate = new RestTemplateBuilder()
                .defaultMessageConverters()
                .setConnectTimeout(2000)
                .additionalInterceptors(new RestTemplateUserAgentInterceptor(), new CoinMarketCapApiKeyInterceptor(apiKey))
                .setReadTimeout(30000)
                .build();
    }


    @Override
    public CmcListingsResult getListings() {
        return restTemplate.getForObject(endpoint + "/v1/cryptocurrency/listings/latest?limit=5000", CmcListingsResult.class);
    }

}
