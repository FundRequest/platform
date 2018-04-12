package io.fundrequest.platform.profile.twitter.model;

import lombok.Data;

import javax.persistence.*;

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
