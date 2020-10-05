package io.fundrequest.core.request.fiat.cryptocompare.client;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class CryptoCompareApiKeyInterceptor implements ClientHttpRequestInterceptor {

    private String apiKey;

    public CryptoCompareApiKeyInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        request.getHeaders().set("authorization", "Apikey " + apiKey);
        return execution.execute(request, body);
    }
}
