package io.fundreqest.platform.tweb.infrastructure.mav;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.statistics.StatisticsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Initializer {

    private final boolean initializeCacheOnStartup;

    public Initializer(@Value("${io.fundrequest.cache.initialize-on-startup:false}") final boolean initializeCacheOnStartup) {
        this.initializeCacheOnStartup = initializeCacheOnStartup;
    }

    @EventListener
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (initializeCacheOnStartup) {
            initialize(event.getApplicationContext());
        }
    }

    private void initialize(final ApplicationContext context) {
        initializeRequests(context);
        initializeTechnologies(context);
        initializeProjects(context);
        initializeStatistics(context);
    }

    private void initializeStatistics(final ApplicationContext context) {
        context.getBean(StatisticsService.class).getStatistics();
    }

    private void initializeProjects(final ApplicationContext context) {
        context.getBean(RequestService.class).findAllProjects();
    }

    private void initializeTechnologies(final ApplicationContext context) {
        context.getBean(RequestService.class).findAllTechnologies();
    }

    private void initializeRequests(final ApplicationContext context) {
        context.getBean(RequestService.class).findAll();
    }
}
