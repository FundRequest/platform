package io.fundrequest.platform.admin.claim.continuous;

import io.fundrequest.core.request.claim.domain.RequestClaim;
import io.fundrequest.core.request.claim.infrastructure.RequestClaimRepository;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static io.fundrequest.core.request.claim.domain.ClaimRequestStatus.APPROVED;

@Component
public class RequestClaimCleaner {

    private final RequestClaimRepository requestClaimRepository;
    private AzraelClient azraelClient;

    public RequestClaimCleaner(final RequestClaimRepository requestClaimRepository,
                               final AzraelClient azraelClient) {
        this.requestClaimRepository = requestClaimRepository;
        this.azraelClient = azraelClient;
    }

    @Scheduled(fixedDelay = 300000L)
    public void clean() {
        final List<RequestClaim> claims = requestClaimRepository.findByStatus(APPROVED);

        claims.stream()
              .filter(x -> {
                  azraelClient.
              })
    }
}
