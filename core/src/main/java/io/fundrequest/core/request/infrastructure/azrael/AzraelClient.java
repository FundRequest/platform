package io.fundrequest.core.request.infrastructure.azrael;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        name = "azrael-client",
        url = "${io.fundrequest.azrael.host}/rest"
)
public interface AzraelClient {

    @RequestMapping(method = RequestMethod.POST, value = "/claims/sign", consumes = MediaType.APPLICATION_JSON_VALUE)
    ClaimSignature getSignature(SignClaimCommand signClaimCommand);

    @RequestMapping(method = RequestMethod.POST, value = "/claims/submit", consumes = MediaType.APPLICATION_JSON_VALUE)
    ClaimTransaction submitClaim(ClaimSignature claimSignature);
}
