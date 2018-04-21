package io.fundrequest.core.request.fund.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FundersDto {

    private List<FunderDto> funders;
    private TotalFundDto fndFunds;
    private TotalFundDto otherFunds;
    private Double usdFunds;

}
