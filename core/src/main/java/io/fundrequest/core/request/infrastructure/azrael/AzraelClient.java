package io.fundrequest.core.request.infrastructure.azrael;


import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        name = "azrael-client",
        url = "${io.fundrequest.azrael.host}/rest"
)
public interface AzraelClient {

    @RequestMapping(method = RequestMethod.POST, value = "/claims", consumes = "application/json;charset=UTF-8")
    ClaimSignature getSignature(SignClaimCommand signClaimCommand);

}
