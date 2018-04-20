package io.fundrequest.db.infrastructure;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

    public String getCurrentAuditor() {

        if (SecurityContextHolder.getContext() != null) {
            org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return null;
            }
            return authentication.getPrincipal().toString();
        }
        return null;
    }
}