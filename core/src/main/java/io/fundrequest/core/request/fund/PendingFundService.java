package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.domain.PendingFund;
import io.fundrequest.core.request.fund.infrastructure.PendingFundRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PendingFundService {

    private final PendingFundRepository pendingFundRepository;

    public PendingFundService(final PendingFundRepository pendingFundRepository) {
        this.pendingFundRepository = pendingFundRepository;
    }

    @Transactional
    public void save(final PendingFund pendingFund) {
        pendingFundRepository.save(pendingFund);
    }
}
