package io.fundrequest.core.request.claim;

import io.fundrequest.core.request.domain.Platform;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class CanClaimRequest {
    @NotNull
    private Platform platform;
    @NotEmpty
    private String platformId;

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CanClaimRequest that = (CanClaimRequest) o;
        return platform == that.platform &&
               Objects.equals(platformId, that.platformId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(platform, platformId);
    }
}
