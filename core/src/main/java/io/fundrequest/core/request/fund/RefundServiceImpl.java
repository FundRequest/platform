package io.fundrequest.core.request.fund;

import io.fundrequest.core.request.fund.command.RequestRefundCommand;
import io.fundrequest.core.request.fund.domain.RefundRequest;
import io.fundrequest.core.request.fund.infrastructure.RefundRequestRepository;
import org.springframework.stereotype.Service;

@Service
public class RefundServiceImpl implements RefundService {

    private final RefundRequestRepository refundRequestRepository;

    public RefundServiceImpl(final RefundRequestRepository refundRequestRepository) {
        this.refundRequestRepository = refundRequestRepository;
    }

    @Override
    public void requestRefund(final RequestRefundCommand requestRefundCommand) {
        refundRequestRepository.save(RefundRequest.builder()
                                                  .requestId(requestRefundCommand.getRequestId())
                                                  .funderAddress(requestRefundCommand.getFunderAddress())
                                                  .r(requestRefundCommand.getR())
                                                  .s(requestRefundCommand.getS())
                                                  .v(requestRefundCommand.getV())
                                                  .build());
    }
}
