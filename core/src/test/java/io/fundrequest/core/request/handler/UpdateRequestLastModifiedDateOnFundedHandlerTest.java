package io.fundrequest.core.request.handler;

import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UpdateRequestLastModifiedDateOnFundedHandlerTest {

    private UpdateRequestLastModifiedDateOnFundedHandler handler;
    private RequestRepository requestRepository;

    @BeforeEach
    void setUp() {
        requestRepository = mock(RequestRepository.class);
        handler = new UpdateRequestLastModifiedDateOnFundedHandler(requestRepository);
    }

    @Test
    void handle_updateLastModifiedDate() {
        final long requestId = 6578L;
        final RequestFundedEvent event = RequestFundedEvent.builder().requestId(requestId).build();
        final Request request = RequestMother.fundRequestArea51()
                                             .withLastModifiedDate(LocalDateTime.now().minusWeeks(3))
                                             .build();

        when(requestRepository.findOne(requestId)).thenReturn(Optional.of(request));

        handler.handle(event);

        assertThat(request.getLastModifiedDate()).isEqualToIgnoringSeconds(LocalDateTime.now());
        verify(requestRepository).saveAndFlush(request);
    }

    @Test
    void handle_requestNotFound() {
        final long requestId = 234;
        final RequestFundedEvent event = RequestFundedEvent.builder().requestId(requestId).build();

        when(requestRepository.findOne(requestId)).thenReturn(Optional.empty());

        try {
            handler.handle(event);
            fail("Expected new RuntimeException(\"Unable to find request\") to be thrown");
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("Unable to find request");
        }
    }
}