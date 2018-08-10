package io.fundrequest.core.request.fund.domain;

import io.fundrequest.core.token.model.TokenValue;
import io.fundrequest.db.infrastructure.AbstractEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Table(name = "fund")
@Entity
@Getter
public class Fund extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "funder_address")
    private String funderAddress;

    @Column(name = "funder_user_id")
    private String funderUserId;

    @Embedded
    private TokenValue tokenValue;

    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "time_stamp")
    private LocalDateTime timestamp;

    @Column(name="blockchain_event_id")
    private Long blockchainEventId;

    protected Fund() {
    }

    @Builder
    Fund(String funderUserId, String funderAddress, TokenValue tokenValue, Long requestId, LocalDateTime timestamp, Long blockchainEventId) {
        this.funderUserId = funderUserId;
        this.funderAddress = funderAddress == null ? null : funderAddress.toLowerCase();
        this.tokenValue = tokenValue;
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.blockchainEventId = blockchainEventId;
    }

    void setId(Long id) {
        this.id = id;
    }

    public void setFunderUserId(String funderUserId) {
        this.funderUserId = funderUserId;
    }
}
