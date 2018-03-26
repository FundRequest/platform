package io.fundrequest.core.request.claim;

import io.fundrequest.core.request.domain.Platform;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
public class UserClaimRequest {
    @NotEmpty
    private String address;
    @NotNull
    private Platform platform;
    @NotEmpty
    private String platformId;

    private UserClaimRequest() {
    }

    @Builder
    public UserClaimRequest(String address, Platform platform, String platformId) {
        this.address = address;
        this.platform = platform;
        this.platformId = platformId;
    }
}
