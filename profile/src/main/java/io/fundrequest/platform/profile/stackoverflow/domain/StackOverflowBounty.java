package io.fundrequest.platform.profile.stackoverflow.domain;

import io.fundrequest.db.infrastructure.AbstractEntity;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "stack_overflow_bounty")
@Getter
public class StackOverflowBounty extends AbstractEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "stack_overflow_id")
    private String stackOverflowId;

    @Column(name = "image")
    private String image;

    @Column(name = "reputation")
    private Long reputation;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Column(name = "valid")
    private Boolean valid;

    StackOverflowBounty() {
    }

    @Builder
    StackOverflowBounty(String userId, String stackOverflowId, String image, Long reputation, String displayName, LocalDateTime createdAt, Boolean valid) {
        this.userId = userId;
        this.stackOverflowId = stackOverflowId;
        this.image = image;
        this.reputation = reputation;
        this.displayName = displayName;
        this.createdAt = createdAt;
        this.valid = valid;
    }
}
