package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.token.dto.TokenValueDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class FundWithUserDto {
    private String funder;
    private TokenValueDto fndFunds;
    private TokenValueDto otherFunds;
    private String funderAddress;
    private boolean isLoggedInUser;
    private boolean isRefund;
    private LocalDateTime timestamp;
}
