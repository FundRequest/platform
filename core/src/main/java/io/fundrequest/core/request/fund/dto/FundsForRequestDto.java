package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.request.fund.UserFundsDto;
import io.fundrequest.core.token.dto.TokenValueDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FundsForRequestDto {

    private List<UserFundsDto> userFunds;
    private TokenValueDto fndFunds;
    private TokenValueDto otherFunds;
    private Double usdFunds;

}
