package io.fundrequest.core.request.claim.dto;

import io.fundrequest.core.token.dto.TokenValueDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClaimsAggregate {

    private List<ClaimDto> claims;
    private TokenValueDto fndValue;
    private TokenValueDto otherValue;
    private Double usdValue;
}
