package io.fundrequest.core.request.claim.domain;

import io.fundrequest.db.infrastructure.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

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

    public void setStatus(ClaimRequestStatus status) {
        this.status = status;
    }
}
