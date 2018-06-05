package io.fundrequest.platform.admin.claim.continuous;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.infrastructure.TrustedRepoRepository;
import io.fundrequest.platform.admin.claim.service.ClaimModerationService;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(value = "io.fundrequest.auto-claimer.enabled", havingValue = "true")
public class AutoClaimer {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoClaimer.class);

    private TrustedRepoRepository trustedRepoRepository;
    private ClaimModerationService claimModerationService;
    private RequestService requestService;

    public AutoClaimer(final ClaimModerationService claimModerationService, TrustedRepoRepository trustedRepoRepository, RequestService requestService) {
        this.claimModerationService = claimModerationService;
        this.trustedRepoRepository = trustedRepoRepository;
        this.requestService = requestService;
    }

    @Scheduled(cron = "0 0 * * * *")
    @Transactional
    public void autoApproveTrustedRepos() {
        Set<String> repos = trustedRepoRepository.findAll().stream().map(r -> r.getOwner().toLowerCase()).collect(Collectors.toSet());
        claimModerationService.listPendingRequestClaims()
                              .stream()
                              .map(r -> Pair.of(r, requestService.findRequest(r.getId())))
                              .filter(r -> repos.contains(r.getRight().getIssueInformation().getOwner().toLowerCase()))
                              .map(Pair::getLeft)
                              .forEach(rc -> {
                                  try {
                                      claimModerationService.approveClaim(rc.getId());
                                  } catch (final Exception ex) {
                                      LOGGER.debug("Unable to approve claim {}", ex.getMessage());
                                  }
                              });
    }
}
