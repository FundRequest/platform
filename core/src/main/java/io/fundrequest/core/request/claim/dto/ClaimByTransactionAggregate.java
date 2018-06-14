package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.token.dto.TokenValueDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(builderClassName = "Builder")
public class ClaimByTransactionAggregate {

    private final String solver;
    private final TokenValueDto fndValue;
    private final TokenValueDto otherValue;
    private final LocalDateTime timestamp;
    private final String transactionHash;
}
