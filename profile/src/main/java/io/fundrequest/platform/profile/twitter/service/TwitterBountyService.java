package io.fundrequest.platform.profile.twitter.service;

import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.profile.bounty.domain.BountyType;
import io.fundrequest.platform.profile.bounty.event.CreateBountyCommand;
import io.fundrequest.platform.profile.bounty.service.BountyService;
import io.fundrequest.platform.profile.twitter.model.TwitterBounty;
import io.fundrequest.platform.profile.twitter.model.TwitterBountyFulfillment;
import io.fundrequest.platform.profile.twitter.model.TwitterBountyType;
import io.fundrequest.platform.profile.twitter.model.TwitterPost;
import io.fundrequest.platform.profile.twitter.repository.TwitterBountyFulfillmentRepository;
import io.fundrequest.platform.profile.twitter.repository.TwitterBountyRepository;
import io.fundrequest.platform.profile.twitter.repository.TwitterPostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class TwitterBountyService {

    private TwitterBountyRepository twitterBountyRepository;
    private Twitter twitter;
    private TwitterPostRepository twitterPostRepository;
    private TwitterBountyFulfillmentRepository twitterBountyFulfillmentRepository;
    private BountyService bountyService;
    private KeycloakRepository keycloakRepository;

    public TwitterBountyService(final TwitterBountyRepository twitterBountyRepository,
                                final Twitter twitter,
                                final TwitterPostRepository twitterPostRepository,
                                final TwitterBountyFulfillmentRepository twitterBountyFulfillmentRepository,
                                final BountyService bountyService,
                                final KeycloakRepository keycloakRepository) {
        this.twitterBountyRepository = twitterBountyRepository;
        this.twitter = twitter;
        this.twitterPostRepository = twitterPostRepository;
        this.twitterBountyFulfillmentRepository = twitterBountyFulfillmentRepository;
        this.bountyService = bountyService;
        this.keycloakRepository = keycloakRepository;
    }

    public List<TwitterPost> getTwitterPosts() {
        return twitterPostRepository.findAll();
    }

    public Optional<TwitterBounty> getActiveBounty() {
        List<TwitterBounty> active = twitterBountyRepository.getActiveTwitterBounties(new Date());
        if (active.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(active.get(0));
        }
    }

    public boolean userIsFollowing(final String user) {
        try {
            return twitter.friendsFollowers().showFriendship(user, "fundrequest_io").isSourceFollowingTarget();
        } catch (final Exception ex) {
            log.error("It would appear as if {} does not exist", user);
            return false;
        }
    }

    public boolean hasFullFilledCurrentBounty(final String username, final String userId, final Principal principal) {
        final List<TwitterBounty> activeTwitterBounties = twitterBountyRepository.getActiveTwitterBounties(new Date());
        if (!activeTwitterBounties.isEmpty()) {
            return hasFullFilled(username, userId, activeTwitterBounties.get(0), principal);
        } else {
            return false;
        }
    }

    private boolean hasFullFilled(final String username, final String userId, final TwitterBounty bounty, final Principal principal) {
        return keycloakRepository.isVerifiedDeveloper(principal.getName()) && bounty.getType().equals(TwitterBountyType.TWEET) && validateTweets(username,
                                                                                                                                                 userId,
                                                                                                                                                 bounty,
                                                                                                                                                 principal);
    }

    private boolean validateTweets(final String username, final String userId, final TwitterBounty bounty, final Principal principal) {

        final List<TwitterPost> posts = twitterPostRepository.findAll();
        try {
            boolean fulfillled = twitter.timelines().getUserTimeline(username)
                                        .stream()
                                        .anyMatch(status -> posts.stream()
                                                                 .anyMatch(post -> status.getText().contains(post.getVerificationText())));
            if (fulfillled) {
                fulfillBounty(username, userId, bounty, principal);
            }
            return fulfillled;
        } catch (final Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    private void fulfillBounty(final String username, final String userId, final TwitterBounty bounty, final Principal principal) {
        final boolean alreadyAdded = claimedBountyAlready(userId, bounty);
        if (!alreadyAdded) {
            final TwitterBountyFulfillment newFullfillment = new TwitterBountyFulfillment();
            newFullfillment.setBounty(bounty);
            newFullfillment.setFulfillmentDate(new Date());
            newFullfillment.setUserId(userId);
            newFullfillment.setUsername(username);
            twitterBountyFulfillmentRepository.save(newFullfillment);
            bountyService.createBounty(
                    CreateBountyCommand
                            .builder()
                            .type(BountyType.TWITTER_TWEET_FOLLOW)
                            .userId(principal.getName())
                            .build()
                                      );
        }
    }

    public boolean claimedBountyAlready(final String userId, final TwitterBounty bounty) {
        return twitterBountyFulfillmentRepository.findByUserIdAndBounty(userId, bounty).isPresent();
    }
}
