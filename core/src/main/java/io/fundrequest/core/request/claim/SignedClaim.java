package io.fundrequest.core.request.claim;

import io.fundrequest.core.request.domain.Platform;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignedClaim {
    private final String solver;
    private final String solverAddress;
    private final Platform platform;
    private final String platformId;
    private final String r;
    private final String s;
    private final Integer v;
}
