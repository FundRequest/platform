package io.fundrequest.core.request.fund.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CreateERC67FundRequest {

    @NotEmpty
    private String platform;
    @NotEmpty
    private String platformId;
    @Min(0)
    private BigDecimal amount;
    @NotNull
    private String tokenAddress;

    @Builder
    public CreateERC67FundRequest(String platform, String platformId, BigDecimal amount, String tokenAddress) {
        this.platform = platform;
        this.platformId = platformId;
        this.amount = amount;
        this.tokenAddress = tokenAddress;
    }

}
