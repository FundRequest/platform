package io.fundrequest.core.request.fund.dto;

import io.fundrequest.core.request.view.AllFundsDto;
import io.fundrequest.core.request.view.IssueInformationDto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PendingFundDto {

    private String description;
    private String transactionId;
    private String fromAddress;
    private String amount;
    private String tokenAddress;
    private IssueInformationDto issueInformation;
    private AllFundsDto funds = new AllFundsDto();

}
