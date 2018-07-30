package io.fundrequest.core.request.fund;

import io.fundrequest.core.token.dto.TokenValueDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserFundsDto {

    private String funderAddress;
    private String funderUserId;
    private TokenValueDto fndFunds;
    private TokenValueDto otherFunds;
    private TokenValueDto fndRefunds;
    private TokenValueDto otherRefunds;

    public boolean hasRefunds() {
        return fndRefunds != null || otherRefunds != null;
    }
}
