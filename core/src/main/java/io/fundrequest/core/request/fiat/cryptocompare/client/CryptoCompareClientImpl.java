package io.fundrequest.core.request.fiat.cryptocompare.client;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.infrastructure.resttemplate.RestTemplateUserAgentInterceptor;
import io.fundrequest.core.request.fiat.cryptocompare.dto.PriceResultDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CryptoCompareClientImpl implements CryptoCompareClient {

    private final RestTemplate restTemplate;
    private static final String endpoint = "https://min-api.cryptocompare.com";

    public CryptoCompareClientImpl(@Value("${cryptocompare.apikey}") String apiKey) {
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        restTemplate = new RestTemplateBuilder()
                .defaultMessageConverters()
                .setConnectTimeout(2000)
                .additionalInterceptors(new RestTemplateUserAgentInterceptor(), new CryptoCompareApiKeyInterceptor(apiKey))
                .setReadTimeout(30000)
                .build();
    }


    public PriceResultDto getPrice(String symbol) {
        return restTemplate.getForObject(endpoint + "/data/price?fsym={symbol}&tsyms=USD", PriceResultDto.class, symbol);
    }
}
