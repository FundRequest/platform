package io.fundrequest.core.infrastructure;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
class SecurityContextServiceImpl implements SecurityContextService {

    @Override
    public Authentication getLoggedInUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

	@Override
	public boolean isUserFullyAuthenticated() {
        Authentication user = this.getLoggedInUser();
        return user != null && user.isAuthenticated() && !(user instanceof AnonymousAuthenticationToken);
	}
}
