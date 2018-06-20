package io.fundreqest.platform.tweb.infrastructure.thymeleaf;

import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
public class ProfilesExpressionObject {

    private final ProfileService profileService;

    public ProfilesExpressionObject(final ProfileService profileService) {
        this.profileService = profileService;
    }

    public Optional<UserProfile> findByUserId(final String userId) {
        return userId == null ? Optional.empty() : Optional.ofNullable(profileService.getUserProfile(userId));
    }

    public Function<UserProfile, String> getName() {
        return UserProfile::getName;
    }
}
