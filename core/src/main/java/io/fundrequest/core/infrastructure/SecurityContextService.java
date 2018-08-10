package io.fundrequest.core.infrastructure;

import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface SecurityContextService {

    Optional<Authentication> getLoggedInUser();

    boolean isUserFullyAuthenticated();

    Optional<UserProfile> getLoggedInUserProfile();
}
