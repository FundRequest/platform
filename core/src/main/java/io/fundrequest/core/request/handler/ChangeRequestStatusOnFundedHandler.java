package io.fundrequest.core.request.handler;

import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ChangeRequestStatusOnFundedHandler {

    private final RequestRepository requestRepository;

    public ChangeRequestStatusOnFundedHandler(final RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @EventListener
    public void handle(final RequestFundedEvent event) {
        final Request request = requestRepository.findOne(event.getRequestId())
                                                 .orElseThrow(() -> new RuntimeException("Unable to find request"));
        if (RequestStatus.OPEN == request.getStatus()) {
            request.setStatus(RequestStatus.FUNDED);
            requestRepository.saveAndFlush(request);
        }
    }
}
