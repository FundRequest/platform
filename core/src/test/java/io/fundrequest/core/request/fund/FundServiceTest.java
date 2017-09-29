package io.fundrequest.core.request.fund;


import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.FundMother;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.fund.command.AddFundsCommand;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.view.FundDtoMother;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigInteger;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FundServiceTest {

    private FundServiceImpl fundService;
    private FundRepository fundRepository;
    private RequestRepository requestRepository;
    private Mappers mappers;
    private ApplicationEventPublisher eventPublisher;

    @Before
    public void setUp() throws Exception {
        fundRepository = mock(FundRepository.class);
        requestRepository = mock(RequestRepository.class);
        mappers = mock(Mappers.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        fundService = new FundServiceImpl(
                fundRepository,
                requestRepository,
                mappers,
                eventPublisher
        );
    }

    @Test
    public void findAll() throws Exception {
        List<Fund> funds = singletonList(FundMother.aFund().build());
        when(fundRepository.findAll()).thenReturn(funds);
        List<FundDto> expecedFunds = singletonList(FundDtoMother.aFundDto());
        when(mappers.mapList(Fund.class, FundDto.class, funds)).thenReturn(expecedFunds);

        List<FundDto> result = fundService.findAll();

        assertThat(result).isEqualTo(expecedFunds);
    }

    @Test
    public void findOne() throws Exception {
        Request request = RequestMother.freeCodeCampNoUserStories().build();

        AddFundsCommand command = new AddFundsCommand();
        command.setRequestId(request.getId());
        command.setAmountInWei(BigInteger.TEN);

        when(requestRepository.findOne(request.getId())).thenReturn(Optional.of(request));

        Principal funder = mock(Principal.class);
        when(funder.getName()).thenReturn("davy");

        fundService.addFunds(funder, command);

        verifyFundsSaved(command, funder);
        verifyEventCreated(command, funder);
    }

    private void verifyEventCreated(AddFundsCommand command, Principal funder) {
        ArgumentCaptor<RequestFundedEvent> requestFundedEventArgumentCaptor = ArgumentCaptor.forClass(RequestFundedEvent.class);
        verify(eventPublisher).publishEvent(requestFundedEventArgumentCaptor.capture());
        assertThat(requestFundedEventArgumentCaptor.getValue().getFunder()).isEqualTo(funder.getName());
        assertThat(requestFundedEventArgumentCaptor.getValue().getRequestId()).isEqualTo(command.getRequestId());
        assertThat(requestFundedEventArgumentCaptor.getValue().getAmountInWei()).isEqualTo(command.getAmountInWei());
    }

    private void verifyFundsSaved(AddFundsCommand command, Principal funder) {
        ArgumentCaptor<Fund> fundArgumentCaptor = ArgumentCaptor.forClass(Fund.class);
        verify(fundRepository).save(fundArgumentCaptor.capture());
        assertThat(fundArgumentCaptor.getValue().getFunder()).isEqualTo(funder.getName());
        assertThat(fundArgumentCaptor.getValue().getRequestId()).isEqualTo(command.getRequestId());
        assertThat(fundArgumentCaptor.getValue().getAmountInWei()).isEqualTo(command.getAmountInWei());
    }
}