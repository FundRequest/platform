package io.fundrequest.core.request.fund.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FunderDto {

    private String funder;
    private TotalFundDto fndFunds;
    private TotalFundDto otherFunds;

}
