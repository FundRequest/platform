package io.fundrequest.core.request.view;

import io.fundrequest.core.token.dto.TokenValueDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllFundsDto {

    private TokenValueDto fndFunds;

    private TokenValueDto otherFunds;

    private Double usdFunds;
}
