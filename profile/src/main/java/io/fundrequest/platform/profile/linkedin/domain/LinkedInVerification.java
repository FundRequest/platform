package io.fundrequest.platform.profile.linkedin.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "linkedin_verification")
@Entity
@Getter
public class LinkedInVerification {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "post_url")
    private String postUrl;

    @Builder
    public LinkedInVerification(String userId, String postUrl) {
        this.userId = userId;
        this.postUrl = postUrl;
    }

    LinkedInVerification() {
    }
}
