package io.fundrequest.core.request.fund.domain;

import io.fundrequest.core.token.model.TokenValue;
import io.fundrequest.db.infrastructure.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

@Entity
@Table(name = "refund")
@Getter
@Setter
@ToString
@EqualsAndHashCode(exclude = "id", callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Refund extends AbstractEntity {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "funder_address")
    private String funderAddress;

    @Column(name = "request_id")
    private Long requestId;

    @Embedded
    private TokenValue tokenValue;

    @Column(name = "blockchain_event_id")
    private Long blockchainEventId;
}
