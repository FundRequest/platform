package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.token.dto.TokenValueDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FundsByFunderDto {
    private String funderAddress;
    private String funderUserId;
    private TokenValueDto fndValue;
    private TokenValueDto otherValue;
}
