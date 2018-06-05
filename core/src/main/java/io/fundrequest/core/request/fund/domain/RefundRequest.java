package io.fundrequest.core.request.fund.domain;

import io.fundrequest.db.infrastructure.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "refund_request")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequest extends AbstractEntity {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "request_id")
    private Long requestId;

    @Column(name = "funder_address")
    private String funderAddress;

    @Column(name = "r")
    private String r;

    @Column(name = "s")
    private String s;

    @Column(name = "v")
    private String v;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private RefundRequestStatus status = RefundRequestStatus.PENDING;

    @Column(name = "transaction_hash")
    @Setter
    private String transactionHash;

    @Column(name = "transaction_submit_time")
    @Setter
    private LocalDateTime transactionSubmitTime;

}
