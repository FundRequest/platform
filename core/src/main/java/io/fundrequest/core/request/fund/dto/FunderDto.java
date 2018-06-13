package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.token.dto.TokenValueDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FunderDto {

    private String funder;
    private TokenValueDto fndFunds;
    private TokenValueDto otherFunds;
    private String funderAddress;
    private boolean isLoggedInUser;
}
