package io.fundrequest.core.request.infrastructure.azrael;


import io.fundrequest.core.transactions.TransactionStatus;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(
        name = "azrael-client",
        url = "${io.fundrequest.azrael.host}"
)
public interface AzraelClient {

    @RequestMapping(method = RequestMethod.POST, value = "/rest/claims", consumes = MediaType.APPLICATION_JSON_VALUE)
    ClaimSignature getSignature(SignClaimCommand signClaimCommand);


    @RequestMapping(method = RequestMethod.GET, value = "/api/v1/transactions/{hash}")
    TransactionStatus getTransactionStatus(@PathVariable("hash") String hash);

}
