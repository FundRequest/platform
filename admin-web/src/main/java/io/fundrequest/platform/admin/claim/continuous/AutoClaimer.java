package io.fundrequest.platform.admin.claim.continuous;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.infrastructure.TrustedRepoRepository;
import io.fundrequest.platform.admin.claim.service.ClaimModerationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(value = "io.fundrequest.auto-claimer.enabled", havingValue = "true")
@Slf4j
public class AutoClaimer {

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
                                      log.debug("Unable to approve claim {}", ex.getMessage());
                                  }
                              });
    }
}
