package io.fundrequest.common.infrastructure.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class BearerAuthRequestInterceptor implements RequestInterceptor {

    private final String headerValue;

    public BearerAuthRequestInterceptor(final String bearerToken) {
        this.headerValue = "Bearer " + bearerToken;
    }

    @Override
    public void apply(final RequestTemplate requestTemplate) {
        requestTemplate.header("Authorization", this.headerValue);
    }
}
