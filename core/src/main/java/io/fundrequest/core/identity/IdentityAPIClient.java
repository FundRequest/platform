package io.fundrequest.core.identity;

import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(
        name = "identity-api-client",
        url = "${io.fundrequest.identity.api.url}"
)
public interface IdentityAPIClient {

    @RequestMapping(value = "/users", params = {"identityProvider", "federatedUsername"}, method = GET)
    UserRepresentation findByIdentityProviderAndFederatedUsername(@RequestParam("identityProvider") String identityProvider, @RequestParam("federatedUsername") String federatedUsername);
}
