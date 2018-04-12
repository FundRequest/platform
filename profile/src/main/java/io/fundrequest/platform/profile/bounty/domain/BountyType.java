package io.fundrequest.platform.profile.bounty.domain;

public enum BountyType {
    REFERRAL(5),
    LINK_GITHUB(15),
    LINK_STACK_OVERFLOW(10),
    POST_LINKEDIN_UPDATE(10),
    TWITTER_TWEET_FOLLOW(10),
    LINK_TELEGRAM(5);
    private final int reward;

    BountyType(int reward) {
        this.reward = reward;
    }

    public int getReward() {
        return reward;
    }
}
