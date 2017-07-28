package io.fundrequest.core.infrastructure;

import org.springframework.security.core.Authentication;

public interface SecurityContextService {
    Authentication getLoggedInUser();
}
