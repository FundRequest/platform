package io.fundreqest.platform.tweb.infrastructure.thymeleaf;

import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

class ProfilesExpressionObjectTest {

    private ProfilesExpressionObject profiles;

    private ProfileService profileService;

    @BeforeEach
    void setUp() {
        profileService = mock(ProfileService.class);
        profiles = new ProfilesExpressionObject(profileService);
    }

    @Test
    public void findByUserId() {
        final String userId = "hgj";
        final UserProfile expected = mock(UserProfile.class);
        when(profileService.getUserProfile(userId)).thenReturn(expected);

        final Optional<UserProfile> result = profiles.findByUserId(userId);

        assertThat(result).contains(expected);
    }

    @Test
    public void findByUserId_profileNull() {
        final String userId = "hgj";
        when(profileService.getUserProfile(userId)).thenReturn(null);

        final Optional<UserProfile> result = profiles.findByUserId(userId);

        assertThat(result).isEmpty();
    }

    @Test
    public void findByUserId_inputNull() {

        final Optional<UserProfile> result = profiles.findByUserId(null);

        verifyZeroInteractions(profileService);
        assertThat(result).isEmpty();
    }

    @Test
    public void getName() {
        final String name = "jhgk";
        final UserProfile userProfile = UserProfile.builder().name(name).build();

        final Function<UserProfile, String> result = profiles.getName();

        assertThat(result.apply(userProfile)).isEqualTo(name);
    }
}