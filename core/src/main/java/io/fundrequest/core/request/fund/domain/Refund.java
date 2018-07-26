package io.fundrequest.core.request.fund.domain;

import io.fundrequest.core.token.model.TokenValue;
import io.fundrequest.db.infrastructure.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "refund")
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = "id", callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class Refund extends AbstractEntity {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "funder_address")
    private String funderAddress;

    @Column(name = "requested_by")
    private String requestedBy;

    @Column(name = "request_id")
    private Long requestId;

    @Embedded
    private TokenValue tokenValue;

    @Column(name = "blockchain_event_id")
    private Long blockchainEventId;


    public static RefundBuilder builder() {
        return new RefundBuilder();
    }

    public static final class RefundBuilder {
        private LocalDateTime creationDate;
        private LocalDateTime lastModifiedDate;
        private String createdBy;
        private Long id;
        private String funderAddress;
        private String lastModifiedBy;
        private Long requestId;
        private String requestedBy;
        private TokenValue tokenValue;
        private Long blockchainEventId;

        private RefundBuilder() {
        }

        public RefundBuilder creationDate(LocalDateTime creationDate) {
            this.creationDate = creationDate;
            return this;
        }

        public RefundBuilder lastModifiedDate(LocalDateTime lastModifiedDate) {
            this.lastModifiedDate = lastModifiedDate;
            return this;
        }

        public RefundBuilder createdBy(String createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public RefundBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RefundBuilder funderAddress(String funderAddress) {
            this.funderAddress = funderAddress;
            return this;
        }

        public RefundBuilder lastModifiedBy(String lastModifiedBy) {
            this.lastModifiedBy = lastModifiedBy;
            return this;
        }

        public RefundBuilder requestId(Long requestId) {
            this.requestId = requestId;
            return this;
        }

        public RefundBuilder requestedBy(String requestedBy) {
            this.requestedBy = requestedBy;
            return this;
        }


        public RefundBuilder tokenValue(TokenValue tokenValue) {
            this.tokenValue = tokenValue;
            return this;
        }

        public RefundBuilder blockchainEventId(Long blockchainEventId) {
            this.blockchainEventId = blockchainEventId;
            return this;
        }

        public RefundBuilder but() {
            return Refund.builder()
                         .creationDate(creationDate)
                         .lastModifiedDate(lastModifiedDate)
                         .createdBy(createdBy)
                         .id(id)
                         .funderAddress(funderAddress)
                         .lastModifiedBy(lastModifiedBy)
                         .requestId(requestId)
                         .tokenValue(tokenValue)
                         .blockchainEventId(blockchainEventId);
        }

        public Refund build() {
            Refund refund = new Refund();
            refund.setCreationDate(creationDate);
            refund.setLastModifiedDate(lastModifiedDate);
            refund.setCreatedBy(createdBy);
            refund.setId(id);
            refund.setFunderAddress(funderAddress);
            refund.setLastModifiedBy(lastModifiedBy);
            refund.setRequestId(requestId);
            refund.setRequestedBy(requestedBy);
            refund.setTokenValue(tokenValue);
            refund.setBlockchainEventId(blockchainEventId);
            return refund;
        }
    }
}
