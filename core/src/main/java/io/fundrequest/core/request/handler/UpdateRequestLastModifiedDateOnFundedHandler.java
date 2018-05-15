package io.fundrequest.core.request.handler;

import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UpdateRequestLastModifiedDateOnFundedHandler {

    private final RequestRepository requestRepository;

    public UpdateRequestLastModifiedDateOnFundedHandler(final RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    @EventListener
    public void handle(final RequestFundedEvent event) {
        final Request request = requestRepository.findOne(event.getRequestId())
                                                 .orElseThrow(() -> new RuntimeException("Unable to find request"));
        request.setLastModifiedDate(LocalDateTime.now());
        requestRepository.saveAndFlush(request);
    }
}
