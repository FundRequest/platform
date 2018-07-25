package io.fundrequest.core.request.claim.domain;

import io.fundrequest.db.infrastructure.AbstractEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "request_claim")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestClaim extends AbstractEntity {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "request_id")
    private Long requestId;
    @Column(name = "address")
    private String address;
    @Column(name = "solver")
    private String solver;
    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    @Builder.Default
    private ClaimRequestStatus status = ClaimRequestStatus.PENDING;
    @Column(name = "flagged")
    private Boolean flagged;

    @Column(name = "transaction_hash")
    @Setter
    private String transactionHash;

    @Column(name = "transaction_submit_time")
    @Setter
    private LocalDateTime transactionSubmitTime;

    public void setStatus(ClaimRequestStatus status) {
        this.status = status;
    }
}
