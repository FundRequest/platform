package io.fundrequest.restapi.config;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(
        name = "civic-auth-client",
        url = "${civic.sip.api.client.endpoint}"
)
public interface CivicAuthClient {

    @RequestMapping(value = "/userdata", method = GET)
    String getIssue(@RequestParam("token") String token);

}
