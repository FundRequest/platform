package io.fundrequest.core.request.infrastructure.azrael;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClaimTransaction {
    private String transactionHash;

    @Builder
    public ClaimTransaction(String transactionHash) {
        this.transactionHash = transactionHash;
    }
}
