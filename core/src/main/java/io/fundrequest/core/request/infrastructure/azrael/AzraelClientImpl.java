package io.fundrequest.core.request.infrastructure.azrael;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.fundrequest.core.infrastructure.resttemplate.RestTemplateUserAgentInterceptor;
import io.fundrequest.core.transactions.TransactionStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.client.support.BasicAuthorizationInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AzraelClientImpl implements AzraelClient {

    private final RestTemplate restTemplate;
    private final String endpoint;


    public AzraelClientImpl(@Value("${io.fundrequest.azrael.host}") String azraelHost,
                            @Value("${feign.client.azrael.username}") final String azraelUsername,
                            @Value("${feign.client.azrael.password}") final String azraelPassword) {
        final ObjectMapper objectMapper = new ObjectMapper();
        this.endpoint = azraelHost;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        restTemplate = new RestTemplateBuilder()
                .defaultMessageConverters()
                .setConnectTimeout(2000)
                .additionalInterceptors(new RestTemplateUserAgentInterceptor(), new BasicAuthorizationInterceptor(azraelUsername, azraelPassword))
                .setReadTimeout(30000)
                .build();
    }

    @Override
    public ClaimSignature getSignature(SignClaimCommand signClaimCommand) {
        return restTemplate.postForObject(endpoint + "/rest/claims/sign", signClaimCommand, ClaimSignature.class);
    }

    @Override
    public ClaimTransaction submitClaim(ClaimSignature claimSignature) {
        return restTemplate.postForObject(endpoint + "/rest/claims/submit", claimSignature, ClaimTransaction.class);
    }

    @Override
    public TransactionStatus getTransactionStatus(String hash) {
        return restTemplate.getForObject(endpoint + "/rest/claims/submit", TransactionStatus.class, hash);
    }

    @Override
    public RefundTransaction submitRefund(RefundCommand refundCommand) {
        return restTemplate.postForObject(endpoint + "/rest/refund/submit", refundCommand, RefundTransaction.class);
    }
}
