package io.fundrequest.platform.tweb.infrastructure.thymeleaf;

import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class ProfilesExpressionObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfilesExpressionObject.class);

    private final ProfileService profileService;

    public ProfilesExpressionObject(final ProfileService profileService) {
        this.profileService = profileService;
    }

    public Optional<UserProfile> findByUserId(final String userId) {
        try {
            return StringUtils.isBlank(userId) ? Optional.empty() : Optional.ofNullable(profileService.getNonLoggedInUserProfile(userId));
        } catch (Exception e) {
            LOGGER.error("Error getting profile for: \"" + userId + "\"", e);
            return Optional.empty();
        }

    }

    public Function<UserProfile, String> getName() {
        return UserProfile::getName;
    }
}