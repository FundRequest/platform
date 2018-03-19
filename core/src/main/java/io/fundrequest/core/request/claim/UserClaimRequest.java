package io.fundrequest.core.request.claim;

import io.fundrequest.core.request.domain.Platform;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class UserClaimRequest {
    @NotEmpty
    private String address;
    @NotNull
    private Platform platform;
    @NotEmpty
    private String platformId;


}
