package io.fundrequest.core.request.view;

import io.fundrequest.core.token.dto.TokenValueDto;
import lombok.Data;

@Data
public class AllFundsDto {

    private TokenValueDto fndFunds;

    private TokenValueDto otherFunds;

    private Double usdFunds;
}
