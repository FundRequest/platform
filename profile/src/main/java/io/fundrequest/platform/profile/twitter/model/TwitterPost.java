package io.fundrequest.platform.profile.twitter.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "twitter_posts")
@Data
public class TwitterPost {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long twitterPost;
    @Column(name = "content")
    private String content;
    @Column(name = "verification_text")
    private String verificationText;
}
