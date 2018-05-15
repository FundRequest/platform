package io.fundrequest.core.request.handler;

import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static io.fundrequest.core.request.domain.RequestStatus.FUNDED;
import static io.fundrequest.core.request.domain.RequestStatus.OPEN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChangeRequestStatusOnFundedHandlerTest {

    private ChangeRequestStatusOnFundedHandler handler;
    private RequestRepository requestRepository;

    @BeforeEach
    void setUp() {
        requestRepository = mock(RequestRepository.class);
        handler = new ChangeRequestStatusOnFundedHandler(requestRepository);
    }

    @Test
    void handle_requestOPEN() {
        final long requestId = 6578L;
        final RequestFundedEvent event = RequestFundedEvent.builder().requestId(requestId).build();
        final Request request = RequestMother.fundRequestArea51().withStatus(OPEN).build();

        when(requestRepository.findOne(requestId)).thenReturn(Optional.of(request));

        handler.handle(event);

        assertThat(request.getStatus()).isEqualTo(FUNDED);
        verify(requestRepository).saveAndFlush(request);
    }

    @ParameterizedTest
    @MethodSource("statusProvider")
    void handle_requestNotOPEN(final RequestStatus status) {
        final long requestId = 987L;
        final RequestFundedEvent event = RequestFundedEvent.builder().requestId(requestId).build();
        final Request request = RequestMother.fundRequestArea51().withStatus(status).build();

        when(requestRepository.findOne(requestId)).thenReturn(Optional.of(request));

        handler.handle(event);

        assertThat(request.getStatus()).isEqualTo(status);
    }

    static Stream<RequestStatus> statusProvider() {
        return Stream.of(RequestStatus.values()).filter(s -> s != OPEN);
    }

    @Test
    void handle_requestNotFound() {
        final long requestId = 908978;
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