package io.fundrequest.core.request.fund.domain;

import io.fundrequest.db.infrastructure.AbstractEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "fund")
@Entity
@Getter
public class Fund extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "funder_address")
    private String funder;

    @Column(name = "funder_user_id")
    private String funderUserId;

    @Column(name = "amount_in_wei")
    private BigDecimal amountInWei;

    @Column(name = "token")
    private String token;

    @Column(name = "request_id")
    private Long requestId;


    @Column(name = "time_stamp")
    private LocalDateTime timestamp;

    protected Fund() {
    }

    @Builder
    Fund(String funderUserId, String funder, BigDecimal amountInWei, String token, Long requestId, LocalDateTime timestamp) {
        this.funderUserId = funderUserId;
        this.funder = funder == null ? null : funder.toLowerCase();
        this.amountInWei = amountInWei;
        this.token = token;
        this.requestId = requestId;
        this.timestamp = timestamp;
    }

    void setId(Long id) {
        this.id = id;
    }

    public void setFunderUserId(String funderUserId) {
        this.funderUserId = funderUserId;
    }
}
