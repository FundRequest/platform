package io.fundrequest.platform.profile.profile.dto;

import io.fundrequest.platform.keycloak.Provider;
import lombok.Builder;
import lombok.Data;

import java.security.Principal;

@Data
@Builder
public class UserLinkedProviderEvent {
    private final Principal principal;
    private final Provider provider;
}
