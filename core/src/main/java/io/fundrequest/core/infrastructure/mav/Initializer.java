package io.fundrequest.core.infrastructure.mav;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.statistics.StatisticsService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Initializer {

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initialize(event.getApplicationContext());
    }

    private void initialize(ApplicationContext context) {
        initializeRequests(context);
        initializeTechnologies(context);
        initializeProjects(context);
        initializeStatistics(context);
    }

    private void initializeStatistics(ApplicationContext context) {
        context.getBean(StatisticsService.class).getStatistics();
    }

    private void initializeProjects(ApplicationContext context) {
        context.getBean(RequestService.class).findAllProjects();
    }

    private void initializeTechnologies(ApplicationContext context) {
        context.getBean(RequestService.class).findAllTechnologies();
    }

    private void initializeRequests(ApplicationContext context) {
        context.getBean(RequestService.class).findAll();
    }

}
