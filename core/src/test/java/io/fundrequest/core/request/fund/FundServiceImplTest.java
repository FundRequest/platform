package io.fundrequest.core.request.fund;


import io.fundrequest.core.contract.service.FundRequestContractsService;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.FundMother;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.request.fund.domain.PendingFund;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import io.fundrequest.core.request.fund.infrastructure.PendingFundRepository;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.view.FundDtoMother;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.token.TokenInfoService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FundServiceImplTest {

    private FundServiceImpl fundService;
    private FundRepository fundRepository;
    private RequestRepository requestRepository;
    private Mappers mappers;
    private ApplicationEventPublisher eventPublisher;
    private TokenInfoService tokenInfoService;
    private CacheManager cacheManager;
    private FundRequestContractsService fundRequestContractsService;
    private PendingFundRepository pendingFundRepository;

    @Before
    public void setUp() {
        fundRepository = mock(FundRepository.class);
        pendingFundRepository = mock(PendingFundRepository.class);
        requestRepository = mock(RequestRepository.class);
        mappers = mock(Mappers.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        cacheManager = mock(CacheManager.class, RETURNS_DEEP_STUBS);
        tokenInfoService = mock(TokenInfoService.class);
        fundRequestContractsService = mock(FundRequestContractsService.class);

        when(fundRepository.saveAndFlush(any(Fund.class))).then(returnsFirstArg());
        fundService = new FundServiceImpl(
                fundRepository,
                pendingFundRepository,
                requestRepository,
                mappers,
                eventPublisher,
                cacheManager,
                tokenInfoService,
                fundRequestContractsService);
    }

    @Test
    public void findAll() {
        List<Fund> funds = singletonList(FundMother.aFund().build());
        when(fundRepository.findAll()).thenReturn(funds);
        List<FundDto> expecedFunds = singletonList(FundDtoMother.aFundDto());
        when(mappers.mapList(Fund.class, FundDto.class, funds)).thenReturn(expecedFunds);

        List<FundDto> result = fundService.findAll();

        assertThat(result).isEqualTo(expecedFunds);
    }

    @Test
    public void findAllByIterable() {
        List<Fund> funds = singletonList(FundMother.aFund().build());
        Set<Long> ids = funds.stream().map(Fund::getId).collect(Collectors.toSet());
        when(fundRepository.findAll(ids)).thenReturn(funds);
        List<FundDto> expecedFunds = singletonList(FundDtoMother.aFundDto());
        when(mappers.mapList(Fund.class, FundDto.class, funds)).thenReturn(expecedFunds);

        List<FundDto> result = fundService.findAll(ids);

        assertThat(result).isEqualTo(expecedFunds);
    }


    @Test
    public void saveFunds() {
        Request request = RequestMother.freeCodeCampNoUserStories().build();

        FundsAddedCommand command = FundsAddedCommand.builder()
                                                     .requestId(request.getId())
                                                     .amountInWei(BigDecimal.TEN)
                                                     .transactionId("trans_id")
                                                     .funderAddress("address")
                                                     .timestamp(LocalDateTime.now())
                                                     .token("token")
                                                     .build();

        when(requestRepository.findOne(request.getId())).thenReturn(Optional.of(request));

        Principal funder = () -> "davy";

        FundDto fundDto = new FundDto();
        when(mappers.map(eq(Fund.class), eq(FundDto.class), any(Fund.class)))
                .thenReturn(fundDto);

        RequestDto requestDto = new RequestDto();
        when(mappers.map(eq(Request.class), eq(RequestDto.class), any(Request.class)))
                .thenReturn(requestDto);
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("funds")).thenReturn(cache);
        when(pendingFundRepository.findByTransactionHash(command.getTransactionId()))
                .thenReturn(Optional.of(PendingFund.builder().userId(funder.getName()).build()));

        fundService.addFunds(command);

        verifyFundsSaved(command, funder);
        verifyEventCreated(requestDto, fundDto);
        verify(cache).evict(request.getId());
    }

    private void verifyEventCreated(RequestDto requestDto, FundDto fundDto) {
        ArgumentCaptor<RequestFundedEvent> requestFundedEventArgumentCaptor = ArgumentCaptor.forClass(RequestFundedEvent.class);
        verify(eventPublisher).publishEvent(requestFundedEventArgumentCaptor.capture());
        assertThat(requestFundedEventArgumentCaptor.getValue().getFundDto()).isEqualTo(fundDto);
        assertThat(requestFundedEventArgumentCaptor.getValue().getRequestDto()).isEqualTo(requestDto);
    }

    private void verifyFundsSaved(FundsAddedCommand command, Principal funder) {
        ArgumentCaptor<Fund> fundArgumentCaptor = ArgumentCaptor.forClass(Fund.class);
        verify(fundRepository).saveAndFlush(fundArgumentCaptor.capture());
        assertThat(fundArgumentCaptor.getValue().getRequestId()).isEqualTo(command.getRequestId());
        assertThat(fundArgumentCaptor.getValue().getAmountInWei()).isEqualTo(command.getAmountInWei());
        assertThat(fundArgumentCaptor.getValue().getCreatedBy()).isEqualTo(funder.getName());
    }
}