package io.fundrequest.core.infrastructure.repository;

import com.auth0.spring.security.api.Auth0UserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityAuditorAware implements AuditorAware<String> {

  public String getCurrentAuditor() {

    org.springframework.security.core.Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return null;
    }

    return authentication.getName();
  }
}