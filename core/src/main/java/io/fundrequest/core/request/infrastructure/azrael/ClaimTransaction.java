package io.fundrequest.core.request.infrastructure.azrael;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClaimTransaction {
    private String transactionHash;
}
