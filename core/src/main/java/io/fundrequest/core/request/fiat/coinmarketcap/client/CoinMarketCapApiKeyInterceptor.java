package io.fundrequest.core.request.fiat.coinmarketcap.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class CoinMarketCapApiKeyInterceptor implements ClientHttpRequestInterceptor {

    private String apiKey;

    public CoinMarketCapApiKeyInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().set("X-CMC_PRO_API_KEY", apiKey);
        return execution.execute(request, body);
    }
}
