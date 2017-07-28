package io.fundrequest.core.infrastructure;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
class SecurityContextServiceImpl implements SecurityContextService {

    @Override
    public Authentication getLoggedInUser() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
}
