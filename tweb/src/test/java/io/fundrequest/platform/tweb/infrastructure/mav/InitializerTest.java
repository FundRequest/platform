package io.fundrequest.platform.tweb.infrastructure.mav;

import io.fundrequest.core.infrastructure.mav.Initializer;
import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.statistics.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class InitializerTest {

    private Initializer initializer;

    @BeforeEach
    void setUp() {
        initializer = new Initializer();
    }

    @Test
    void initializesCachedData() {
        ContextRefreshedEvent event = mock(ContextRefreshedEvent.class);
        ApplicationContext context = mock(ApplicationContext.class);
        when(event.getApplicationContext()).thenReturn(context);
        RequestService requestService = mock(RequestService.class);
        when(context.getBean(RequestService.class)).thenReturn(requestService);
        StatisticsService statisticsService = mock(StatisticsService.class);
        when(context.getBean(StatisticsService.class)).thenReturn(statisticsService);

        initializer.onApplicationEvent(event);

        verify(requestService).findAll();
        verify(requestService).findAllTechnologies();
        verify(requestService).findAllProjects();
        verify(statisticsService).getStatistics();
    }
}