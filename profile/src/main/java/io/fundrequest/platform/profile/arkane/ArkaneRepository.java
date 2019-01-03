package io.fundrequest.platform.profile.arkane;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@FeignClient(
        name = "arkane-client",
        url = "${network.arkane.api.endpoint}"
)
public interface ArkaneRepository {
    @RequestMapping(value = "/wallets", method = GET)
    WalletsResult getWallets(@RequestHeader("Authorization") String bearerAuthorizationHeader);
}
