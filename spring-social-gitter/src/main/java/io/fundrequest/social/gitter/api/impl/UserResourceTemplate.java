package io.fundrequest.social.gitter.api.impl;

import io.fundrequest.social.gitter.api.Me;
import io.fundrequest.social.gitter.api.UserResource;
import org.springframework.web.client.RestTemplate;

public class UserResourceTemplate implements UserResource {

    private RestTemplate restTemplate;

    public UserResourceTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Me me() {
        return restTemplate.getForObject("https://api.gitter.im/v1/user/me", Me.class);
    }
}
