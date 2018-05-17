package io.fundrequest.core.request.infrastructure.azrael;


import io.fundrequest.core.transactions.TransactionStatus;
import io.fundrequest.platform.github.GithubFeignConfiguration;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        name = "azrael-client",
        url = "${io.fundrequest.azrael.host}",
        configuration = AzraelFeignConfiguration.class
)
public interface AzraelClient {

    @RequestMapping(method = RequestMethod.POST, value = "/rest/claims/sign", consumes = MediaType.APPLICATION_JSON_VALUE)
    ClaimSignature getSignature(SignClaimCommand signClaimCommand);

    @RequestMapping(method = RequestMethod.POST, value = "/rest/claims/submit", consumes = MediaType.APPLICATION_JSON_VALUE)
    ClaimTransaction submitClaim(ClaimSignature claimSignature);

    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/transactions/{hash}")
    TransactionStatus getTransactionStatus(@PathVariable("hash") String hash);
}
