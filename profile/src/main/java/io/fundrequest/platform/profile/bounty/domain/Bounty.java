package io.fundrequest.platform.profile.bounty.domain;

import io.fundrequest.core.infrastructure.repository.AbstractEntity;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "bounty")
@NoArgsConstructor
public class Bounty extends AbstractEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "type")
    @Enumerated(value = EnumType.STRING)
    private BountyType type;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private BountyStatus status;



    @Builder
    Bounty(String userId, BountyType type) {
        this.userId = userId;
        this.type = type;
        this.status = BountyStatus.PENDING;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public BountyType getType() {
        return type;
    }

    public BountyStatus getStatus() {
        return status;
    }
}
