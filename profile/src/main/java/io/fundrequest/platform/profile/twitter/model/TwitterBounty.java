package io.fundrequest.platform.profile.twitter.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "twitter_bounties")
public class TwitterBounty {

    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private TwitterBountyType type;
    @Column(name = "follow_account")
    private String followAccount;
    @Column(name = "required_posts")
    private int requiredPosts = 1;
}
