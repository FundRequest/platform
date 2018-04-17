package io.fundrequest.core.fund.domain;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;

@Data
@Entity(name = "pending_funds")
public class PendingFund {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_hash")
    private String transactionHash;

    @Column(name = "description")
    private String description;

    @Column(name = "address_from")
    private String fromAddress;

    @Column(name = "amount")
    private BigInteger amount;

    @Column(name = "token_address")
    private String tokenAddress;

    @Builder
    public PendingFund(final String transactionhash, final String description, final String fromAddress, final BigInteger amount, final String tokenAddress) {
        this.transactionHash = transactionhash;
        this.description = description;
        this.fromAddress = fromAddress;
        this.amount = amount;
        this.tokenAddress = tokenAddress;
    }
}
