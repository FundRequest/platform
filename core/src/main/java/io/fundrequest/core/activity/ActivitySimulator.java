package io.fundrequest.core.activity;

import io.fundrequest.core.request.domain.RequestSource;
import io.fundrequest.core.request.event.RequestCreatedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ActivitySimulator {

    private ApplicationEventPublisher eventPublisher;

    private static Long COUNTER = 0L;

    public ActivitySimulator(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Scheduled(fixedRate = 1000)
    @Transactional
    public void testActivity() {
        if(COUNTER.equals(Long.MAX_VALUE)) {
            COUNTER = 0L;
        }
        eventPublisher.publishEvent(new RequestCreatedEvent("davy", "http://github.com", "Github issue " + COUNTER++, RequestSource.GITHUB));
    }
}
