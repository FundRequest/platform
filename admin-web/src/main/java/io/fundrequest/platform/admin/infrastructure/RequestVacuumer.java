package io.fundrequest.platform.admin.infrastructure;

import io.fundrequest.core.request.claim.domain.ClaimRequestStatus;
import io.fundrequest.core.request.claim.domain.RequestClaim;
import io.fundrequest.core.request.claim.infrastructure.RequestClaimRepository;
import io.fundrequest.core.request.fund.domain.RefundRequest;
import io.fundrequest.core.request.fund.domain.RefundRequestStatus;
import io.fundrequest.core.request.fund.infrastructure.RefundRequestRepository;
import io.fundrequest.core.request.infrastructure.azrael.AzraelClient;
import io.fundrequest.core.transactions.TransactionStatus;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(value = "io.fundrequest.request-vacuumer.enabled", havingValue = "true")
public class RequestVacuumer {

    private final RequestClaimRepository requestClaimRepository;
    private final RefundRequestRepository refundRequestRepository;
    private AzraelClient azraelClient;

    public RequestVacuumer(final RequestClaimRepository requestClaimRepository,
                           final RefundRequestRepository refundRequestRepository,
                           final AzraelClient azraelClient) {
        this.requestClaimRepository = requestClaimRepository;
        this.refundRequestRepository = refundRequestRepository;
        this.azraelClient = azraelClient;
    }

    @Scheduled(fixedDelay = 300_000L)
    public void cleanClaims() {
        final List<RequestClaim> claims = requestClaimRepository.findByStatus(ClaimRequestStatus.APPROVED);

        claims.stream()
              .filter(x -> x.getTransactionHash() != null)
              .filter(x -> azraelClient.getTransactionStatus(x.getTransactionHash()).equals(TransactionStatus.FAILED))
              .forEach(x -> {
                  x.setStatus(ClaimRequestStatus.TRANSACTION_FAILED);
                  requestClaimRepository.save(x);
              });
    }

    @Scheduled(fixedDelay = 300_000L)
    public void cleanRefunds() {
        final List<RefundRequest> refundRequests = refundRequestRepository.findAllByStatus(RefundRequestStatus.APPROVED);

        refundRequests.stream()
                      .filter(refundRequest -> refundRequest.getTransactionHash() != null)
                      .filter(refundRequest -> azraelClient.getTransactionStatus(refundRequest.getTransactionHash()).equals(TransactionStatus.FAILED))
                      .forEach(refundRequest -> {
                          refundRequest.setStatus(RefundRequestStatus.TRANSACTION_FAILED);
                          refundRequestRepository.save(refundRequest);
                      });
    }
}
