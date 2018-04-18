package io.fundrequest.core.fund.domain;

import io.fundrequest.core.request.domain.Platform;
import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.math.BigInteger;

@Data
@Entity(name = "pending_funds")
public class PendingFund {

    @Id
    @Column(name = "id")
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

    @Enumerated(value = EnumType.STRING)
    @Column(name = "platform")
    private Platform platform;

    @Column(name = "platform_id")
    private String platformId;

    @Builder
    public PendingFund(final String transactionhash,
                       final String description,
                       final String fromAddress,
                       final BigInteger amount,
                       final String tokenAddress,
                       final String userId,
                       final Platform platform,
                       final String platformId) {
        this.transactionHash = transactionhash;
        this.description = description;
        this.fromAddress = fromAddress;
        this.amount = amount;
        this.tokenAddress = tokenAddress;
        this.userId = userId;
        this.platform = platform;
        this.platformId = platformId;
    }
}
