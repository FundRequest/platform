package io.fundrequest.platform.profile.twitter.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Table(name = "twitter_bounty_fulfillments")
@Entity
@Data
public class TwitterBountyFulfillment {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    @Column(name = "user_id")
    private String userId;
    @ManyToOne
    @JoinColumn(name = "bounty_id")
    private TwitterBounty bounty;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fulfillment_date")
    private Date fulfillmentDate;
}
