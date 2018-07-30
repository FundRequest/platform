package io.fundrequest.core.request.fund.domain;

import io.fundrequest.db.infrastructure.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@Setter
@ToString
@EqualsAndHashCode(exclude = "id", callSuper = false)
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

    @Column(name = "requested_by")
    private String requestedBy;

    @Builder.Default
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private RefundRequestStatus status = RefundRequestStatus.PENDING;

    @Column(name = "transaction_hash")
    private String transactionHash;

    @Column(name = "transaction_submit_time")
    private LocalDateTime transactionSubmitTime;
}
