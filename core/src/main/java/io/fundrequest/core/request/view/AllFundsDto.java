package io.fundrequest.core.request.view;

import io.fundrequest.core.request.fund.dto.TotalFundDto;
import lombok.Data;

@Data
public class AllFundsDto {

    private TotalFundDto fndFunds;

    private TotalFundDto otherFunds;

    private Double usdFunds;
}
