package io.fundrequest.core.infrastructure;

import io.fundrequest.common.infrastructure.SecurityContextHolderSpringDelegate;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
class SecurityContextServiceImpl implements SecurityContextService {

    private final SecurityContextHolderSpringDelegate securityContextHolder;
    private final ProfileService profileService;

    SecurityContextServiceImpl(final SecurityContextHolderSpringDelegate securityContextHolder, final ProfileService profileService) {
        this.securityContextHolder = securityContextHolder;
        this.profileService = profileService;
    }

    @Override
    public Optional<Authentication> getLoggedInUser() {
        return Optional.ofNullable(securityContextHolder.getContext().getAuthentication());
    }

	@Override
	public boolean isUserFullyAuthenticated() {
        return isUserFullyAuthenticated(securityContextHolder.getContext().getAuthentication());
    }

    private boolean isUserFullyAuthenticated(final Authentication loggedInUser) {
        return Optional.ofNullable(loggedInUser)
                       .filter(Authentication::isAuthenticated)
                       .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken))
                       .isPresent();
    }

    @Override
    public Optional<UserProfile> getLoggedInUserProfile() {
        return getLoggedInUser().filter(this::isUserFullyAuthenticated)
                                .map(authentication -> (Principal) authentication.getPrincipal())
                                .map(profileService::getUserProfile);
    }
}
