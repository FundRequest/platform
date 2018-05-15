package io.fundrequest.platform.admin.claim.service;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.claim.infrastructure.TrustedRepoRepository;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AutoClaimServiceImpl implements AutoClaimService {

    private TrustedRepoRepository trustedRepoRepository;
    private ClaimModerationService claimModerationService;
    private RequestService requestService;

    public AutoClaimServiceImpl(final ClaimModerationService claimModerationService, TrustedRepoRepository trustedRepoRepository, RequestService requestService) {
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
                              .forEach(rc -> claimModerationService.approveClaim(rc.getId()));
    }
}
