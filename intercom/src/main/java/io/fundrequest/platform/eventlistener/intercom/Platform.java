package io.fundrequest.platform.eventlistener.intercom;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface Platform {

    String PLATFORM_EVENTS = "platformEvents";

    @Input(Platform.PLATFORM_EVENTS)
    SubscribableChannel platformEvents();
}
