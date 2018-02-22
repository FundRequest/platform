package io.fundrequest.core.request.infrastructure.azrael;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

@Builder
@Data
public class SignClaimCommand {

    @NotEmpty
    private String platform;
    @NotEmpty
    private String platformId;
    @NotEmpty
    private String solver;
    @NotEmpty
    private String address;

}
