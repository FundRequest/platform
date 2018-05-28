package io.fundrequest.core.token.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TokenValue implements Serializable {
    private static final long serialVersionUID = 3367000560837244733L;

    @Column(name = "token_hash")
    private String tokenAddress;
    @Column(name = "amount_in_wei")
    private BigDecimal amountInWei;

}
