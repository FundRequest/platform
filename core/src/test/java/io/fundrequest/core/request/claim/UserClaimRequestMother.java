package io.fundrequest.core.request.claim;

import io.fundrequest.core.request.domain.Platform;

public final class UserClaimRequestMother {

    public static UserClaimRequest.UserClaimRequestBuilder kazuki43zooApiStub() {
        return UserClaimRequest
                .builder()
                .platform(Platform.GITHUB)
                .platformId("kazuki43zoo|FR|api-stub|FR|42")
                .address("0x123f681646d4a755815f9cb19e1acc8565a0c2ac");
    }
}