package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.token.dto.TokenValueDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClaimsByTransactionAggregate {

    private final List<ClaimByTransactionAggregate> claims;
    private final TokenValueDto fndValue;
    private final TokenValueDto otherValue;
    private final Double usdValue;
}
