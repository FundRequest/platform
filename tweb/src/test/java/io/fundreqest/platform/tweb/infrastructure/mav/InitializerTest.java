package io.fundreqest.platform.tweb.infrastructure.mav;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.statistics.StatisticsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

public class InitializerTest {

    private Initializer initializer;

    @BeforeEach
    void setUp() {
        initializer = new Initializer(true);
    }

    @Test
    void initializesCachedData() {
        final ContextRefreshedEvent event = mock(ContextRefreshedEvent.class);
        final ApplicationContext context = mock(ApplicationContext.class);
        final RequestService requestService = mock(RequestService.class);
        final StatisticsService statisticsService = mock(StatisticsService.class);

        when(event.getApplicationContext()).thenReturn(context);
        when(context.getBean(RequestService.class)).thenReturn(requestService);
        when(context.getBean(StatisticsService.class)).thenReturn(statisticsService);

        initializer.onApplicationEvent(event);

        verify(requestService).findAll();
        verify(requestService).findAllTechnologies();
        verify(requestService).findAllProjects();
        verify(statisticsService).getStatistics();
    }

    @Test
    void initializesCachedData_initializeFalse() {
        final ContextRefreshedEvent event = mock(ContextRefreshedEvent.class);
        final ApplicationContext context = mock(ApplicationContext.class);
        final RequestService requestService = mock(RequestService.class);
        final StatisticsService statisticsService = mock(StatisticsService.class);

        when(event.getApplicationContext()).thenReturn(context);
        when(context.getBean(RequestService.class)).thenReturn(requestService);
        when(context.getBean(StatisticsService.class)).thenReturn(statisticsService);

        new Initializer(false).onApplicationEvent(event);

        verifyZeroInteractions(requestService);
        verifyZeroInteractions(requestService);
        verifyZeroInteractions(requestService);
        verifyZeroInteractions(statisticsService);
    }
}