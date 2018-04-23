package io.fundreqest.platform.tweb.profile.twitter;

import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfileProvider;
import io.fundrequest.platform.profile.twitter.dto.ValidatedBountyDto;
import io.fundrequest.platform.profile.twitter.service.TwitterBountyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@RequestMapping(value = "/bounties/twitter")
@Controller
public class TwitterBountyController {


    @Autowired
    private ProfileService profileService;
    @Autowired
    private TwitterBountyService twitterbountyService;

    @GetMapping(value = "/verify", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ValidatedBountyDto validateBounty(final HttpServletRequest request, final Principal principal, final RedirectAttributes redirectAttributes) {
        String message;
        boolean validated = false;

        boolean isFollowing = isFollowing(principal);
        if (!isFollowing) {
            message = "You are not following us yet. Please follow us before claiming.";
        } else {
            boolean hasFulfilled = hasFullfilledCurrentBounty(principal);
            if (hasFulfilled) {
                validated = true;
                message = "Successfully validated your tweet.";
            } else {
                message = "Tweet was not found in your last 20 tweets.";
            }
        }
        return ValidatedBountyDto.builder().message(message).validated(validated).build();
    }


    private boolean hasFullfilledCurrentBounty(final Principal principal) {
        try {
            final UserProfileProvider twitterProvider = profileService.getUserProfile(principal).getTwitter();
            return twitterbountyService.hasFullFilledCurrentBounty(twitterProvider.getUsername(), twitterProvider.getUserId(), principal);
        } catch (final Exception ex) {
            return false;
        }
    }

    private boolean isFollowing(final Principal principal) {
        try {
            return twitterbountyService.userIsFollowing(profileService.getUserProfile(principal).getTwitter().getUsername());
        } catch (final Exception ex) {
            return false;
        }
    }
}
