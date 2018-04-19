package io.fundrequest.platform.profile.github.domain;

import io.fundrequest.db.infrastructure.AbstractEntity;
import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "github_bounty")
public class GithubBounty extends AbstractEntity {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "github_id")
    private String githubId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "location")
    private String location;

    @Column(name = "valid")
    private Boolean valid;

    GithubBounty() {
    }

    @Builder
    GithubBounty(String userId, String githubId, LocalDateTime createdAt, String location, Boolean valid) {
        this.userId = userId;
        this.githubId = githubId;
        this.createdAt = createdAt;
        this.location = location;
        this.valid = valid;
    }

    public Long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getGithubId() {
        return githubId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getLocation() {
        return location;
    }

    public Boolean getValid() {
        return valid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GithubBounty that = (GithubBounty) o;
        return Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userId);
    }
}
