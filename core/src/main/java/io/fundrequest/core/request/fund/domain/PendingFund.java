package io.fundrequest.core.request.fund.domain;

import io.fundrequest.core.request.domain.IssueInformation;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

    @Column(name = "user_id")
    private String userId;

    @Embedded
    private IssueInformation issueInformation;

    @Builder
    public PendingFund(final String transactionhash,
                       final String description,
                       final String fromAddress,
                       final BigInteger amount,
                       final String tokenAddress,
                       final String userId,
                       final IssueInformation issueInformation) {
        this.transactionHash = transactionhash;
        this.description = description;
        this.fromAddress = fromAddress;
        this.amount = amount;
        this.tokenAddress = tokenAddress;
        this.userId = userId;
        this.issueInformation = issueInformation;
    }

    protected PendingFund() {
    }
}
