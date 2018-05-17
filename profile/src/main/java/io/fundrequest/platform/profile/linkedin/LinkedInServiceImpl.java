package io.fundrequest.platform.profile.linkedin;

import io.fundrequest.platform.keycloak.KeycloakRepository;
import io.fundrequest.platform.keycloak.Provider;
import io.fundrequest.platform.profile.linkedin.dto.LinkedInVerificationDto;
import io.fundrequest.platform.profile.linkedin.infrastructure.LinkedInClient;
import io.fundrequest.platform.profile.linkedin.infrastructure.LinkedInUser;
import io.fundrequest.platform.profile.linkedin.infrastructure.LinkedInVerificationRepository;
import io.fundrequest.platform.profile.profile.dto.UserLinkedProviderEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Slf4j
class LinkedInServiceImpl implements LinkedInService {

    private KeycloakRepository keycloakRepository;
    private LinkedInClient client;
    private LinkedInVerificationRepository repository;

    public LinkedInServiceImpl(KeycloakRepository keycloakRepository,
                               LinkedInClient client,
                               LinkedInVerificationRepository repository) {
        this.keycloakRepository = keycloakRepository;
        this.client = client;
        this.repository = repository;
    }

    @Override
    @Transactional(readOnly = true)
    public LinkedInVerificationDto getVerification(Principal principal) {
        return repository.findByUserId(principal.getName()).map(
                l -> LinkedInVerificationDto.builder()
                                            .verified(true)
                                            .postUrl(l.getPostUrl())
                                            .build()
                                                               ).orElseGet(() -> null);
    }

    @EventListener
    @Transactional
    public void onProviderLinked(UserLinkedProviderEvent event) {
        if (event.getProvider() == Provider.LINKEDIN && event.getPrincipal() != null) {
            try {
                updateUserHeadline(event);
            } catch (Exception e) {
                log.error("An error occurred when getting users headline from LinkedIn");
            }
        }
    }

    private void updateUserHeadline(UserLinkedProviderEvent event) {
        LinkedInUser linkedInUser = client.getUserInfo(keycloakRepository.getAccessToken((KeycloakAuthenticationToken) event.getPrincipal(), Provider.LINKEDIN));
        if (linkedInUser != null && StringUtils.isNotBlank(linkedInUser.getHeadline())) {
            keycloakRepository.updateHeadline(event.getPrincipal().getName(), linkedInUser.getHeadline());
        }
    }

}
