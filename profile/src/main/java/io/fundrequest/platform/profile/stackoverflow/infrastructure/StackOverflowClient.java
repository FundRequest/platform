package io.fundrequest.platform.profile.stackoverflow.infrastructure;

import io.fundrequest.platform.profile.stackoverflow.dto.StackOverflowResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(
        name = "github-client",
        url = "http://api.stackexchange.com/2.2/"
)
public interface StackOverflowClient {

    @RequestMapping(value = "/users/{userid}?site=stackoverflow&key=${feign.client.stackoverflow.key}", method = GET)
    StackOverflowResult getUser(
            @PathVariable("userid") String userId
                               );

}
