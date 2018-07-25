package io.fundrequest.core.request.fund.handler;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.command.UpdateRequestStatusCommand;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.RefundProcessedEvent;
import io.fundrequest.core.request.fund.domain.Refund;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestDtoMother;
import io.fundrequest.core.token.dto.TokenValueDtoMother;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static io.fundrequest.core.request.domain.RequestStatus.FUNDED;
import static io.fundrequest.core.request.domain.RequestStatus.OPEN;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

class CheckFundsGtZeroHandlerTest {

    private CheckFundsGtZeroHandler handler;
    private RequestService requestService;
    private FundService fundService;

    @BeforeEach
    void setUp() {
        requestService = mock(RequestService.class);
        fundService = mock(FundService.class);
        handler = new CheckFundsGtZeroHandler(fundService, requestService);
    }

    @Test
    void onRefund_fundsPresent() {
        final long requestId = 254L;

        when(fundService.getTotalFundsForRequest(requestId)).thenReturn(Arrays.asList(TokenValueDtoMother.FND().totalAmount(new BigDecimal("45")).build(),
                                                                                      TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("23")).build()));

        handler.handleRefund(new RefundProcessedEvent(Refund.builder().requestId(requestId).build()));

        verifyZeroInteractions(requestService);
    }

    @Test
    void onRefund_noMoreFundsPresent() {
        final long requestId = 32L;
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        requestDto.setStatus(FUNDED);

        when(fundService.getTotalFundsForRequest(requestId)).thenReturn(Arrays.asList(TokenValueDtoMother.FND().totalAmount(new BigDecimal("0")).build(),
                                                                                      TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("0")).build()));
        when(requestService.findRequest(requestId)).thenReturn(requestDto);

        handler.handleRefund(new RefundProcessedEvent(Refund.builder().requestId(requestId).build()));

        verify(requestService).update(new UpdateRequestStatusCommand(requestId, OPEN));
    }

    @Test
    void onRefund_nothingPresent() {
        final long requestId = 87L;
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        requestDto.setStatus(FUNDED);

        when(fundService.getTotalFundsForRequest(requestId)).thenReturn(new ArrayList<>());
        when(requestService.findRequest(requestId)).thenReturn(requestDto);

        handler.handleRefund(new RefundProcessedEvent(Refund.builder().requestId(requestId).build()));

        verify(requestService).update(new UpdateRequestStatusCommand(requestId, OPEN));
    }

    @Test
    void onRefund_statusNotFUNDED() {
        final long requestId = 254L;
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        requestDto.setStatus(OPEN);

        when(fundService.getTotalFundsForRequest(requestId)).thenReturn(Arrays.asList(TokenValueDtoMother.FND().totalAmount(new BigDecimal("0")).build(),
                                                                                      TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("0")).build()));
        when(requestService.findRequest(requestId)).thenReturn(requestDto);

        handler.handleRefund(new RefundProcessedEvent(Refund.builder().requestId(requestId).build()));

        verify(requestService).findRequest(requestId);
        verifyNoMoreInteractions(requestService);
    }




    @Test
    void onFund_fundsPresent() {
        final long requestId = 254L;
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        requestDto.setStatus(OPEN);

        when(fundService.getTotalFundsForRequest(requestId)).thenReturn(Arrays.asList(TokenValueDtoMother.FND().totalAmount(new BigDecimal("45")).build(),
                                                                                      TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("23")).build()));
        when(requestService.findRequest(requestId)).thenReturn(requestDto);

        handler.handleFund(RequestFundedEvent.builder().requestId(requestId).build());

        verify(requestService).update(new UpdateRequestStatusCommand(requestId, FUNDED));
    }

    @Test
    void onFund_statusNotOPEN() {
        final long requestId = 254L;
        final RequestDto requestDto = RequestDtoMother.fundRequestArea51();
        requestDto.setStatus(FUNDED);

        when(fundService.getTotalFundsForRequest(requestId)).thenReturn(Arrays.asList(TokenValueDtoMother.FND().totalAmount(new BigDecimal("45")).build(),
                                                                                      TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("23")).build()));
        when(requestService.findRequest(requestId)).thenReturn(requestDto);

        handler.handleFund(RequestFundedEvent.builder().requestId(requestId).build());

        verify(requestService).findRequest(requestId);
        verifyNoMoreInteractions(requestService);
    }

    @Test
    void onFund_noMoreFundsPresent() {
        final long requestId = 32L;

        when(fundService.getTotalFundsForRequest(requestId)).thenReturn(Arrays.asList(TokenValueDtoMother.FND().totalAmount(new BigDecimal("0")).build(),
                                                                                      TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("0")).build()));

        handler.handleFund(RequestFundedEvent.builder().requestId(requestId).build());

        verifyZeroInteractions(requestService);
    }

    @Test
    void onFund_nothingPresent() {
        final long requestId = 87L;

        when(fundService.getTotalFundsForRequest(requestId)).thenReturn(new ArrayList<>());

        handler.handleFund(RequestFundedEvent.builder().requestId(requestId).build());

        verifyZeroInteractions(requestService);
    }
}
