package io.fundrequest.core.fund;

import io.fundrequest.core.fund.domain.PendingFund;
import io.fundrequest.core.fund.repository.PendingFundRepository;
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
