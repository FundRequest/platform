package io.fundrequest.core.request.fund.command;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class FundsAddedCommand {

    @NotNull
    private Long requestId;

    @NotNull
    @DecimalMin(value = "0")
    private BigDecimal amountInWei;

    private String transactionId;

    private String token;

    private LocalDateTime timestamp;

}
