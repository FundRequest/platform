package io.fundrequest.core.request.fund;


import io.fundrequest.core.contract.service.FundRequestContractsService;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.FundMother;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.fiat.FiatService;
import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.request.fund.domain.PendingFund;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.dto.FundersDto;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import io.fundrequest.core.request.fund.infrastructure.PendingFundRepository;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.view.FundDtoMother;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import io.fundrequest.core.token.dto.TokenInfoDtoMother;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import io.fundrequest.platform.profile.profile.dto.UserProfileMother;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationEventPublisher;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
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
    private ProfileService profileService;
    private FiatService fiatService;

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
        profileService = mock(ProfileService.class);
        fiatService = mock(FiatService.class);
        when(fundRepository.saveAndFlush(any(Fund.class))).then(returnsFirstArg());
        fundService = new FundServiceImpl(
                fundRepository,
                pendingFundRepository,
                requestRepository,
                mappers,
                eventPublisher,
                cacheManager,
                tokenInfoService,
                fundRequestContractsService,
                profileService,
                fiatService);
    }

    @Test
    public void findAll() {
        List<Fund> funds = singletonList(FundMother.fndFundFunderKnown().build());
        when(fundRepository.findAll()).thenReturn(funds);
        List<FundDto> expecedFunds = singletonList(FundDtoMother.aFundDto());
        when(mappers.mapList(Fund.class, FundDto.class, funds)).thenReturn(expecedFunds);

        List<FundDto> result = fundService.findAll();

        assertThat(result).isEqualTo(expecedFunds);
    }

    @Test
    public void findAllByIterable() {
        List<Fund> funds = singletonList(FundMother.fndFundFunderKnown().build());
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

    @Test
    public void fundedByHasNameFunder() {
        List<Fund> funds = Collections.singletonList(FundMother.fndFundFunderKnown().build());
        when(fundRepository.findByRequestId(1L)).thenReturn(funds);
        mockTokenInfo();

        UserProfile davy = UserProfileMother.davy();
        when(profileService.getUserProfile(funds.get(0).getFunderUserId())).thenReturn(davy);

        FundersDto result = fundService.getFundedBy(1L);

        assertThat(result.getFunders().get(0).getFunder()).isEqualTo(davy.getName());
    }

    @Test
    public void fundedByHasNameFunderAddress() {
        List<Fund> funds = Collections.singletonList(FundMother.fndFundFunderNotKnown().build());
        when(fundRepository.findByRequestId(1L)).thenReturn(funds);
        mockTokenInfo();

        FundersDto result = fundService.getFundedBy(1L);

        assertThat(result.getFunders().get(0).getFunder()).isEqualTo(funds.get(0).getFunder());
    }

    @Test
    public void fundedByHasTotalFnd() {
        List<Fund> funds = Arrays.asList(
                FundMother.fndFundFunderNotKnown().amountInWei(new BigDecimal("1000000000000000000")).build(),
                FundMother.fndFundFunderNotKnown().amountInWei(new BigDecimal("2000000000000000000")).build()
                                        );
        when(fundRepository.findByRequestId(1L)).thenReturn(funds);
        mockTokenInfo();

        FundersDto result = fundService.getFundedBy(1L);

        assertThat(result.getOtherFunds()).isNull();
        assertThat(result.getFndFunds().getTokenSymbol()).isEqualTo("FND");
        assertThat(result.getFndFunds().getTokenAddress()).isEqualTo(funds.get(0).getToken());
        assertThat(result.getFndFunds().getTotalAmount()).isEqualByComparingTo("3");
    }

    @Test
    public void fundedByHasTotalOther() {
        List<Fund> funds = Arrays.asList(
                FundMother.zrxFundFunderNotKnown().amountInWei(new BigDecimal("1000000000000000000")).build(),
                FundMother.zrxFundFunderNotKnown().amountInWei(new BigDecimal("2000000000000000000")).build()
                                        );
        when(fundRepository.findByRequestId(1L)).thenReturn(funds);
        mockTokenInfo();

        FundersDto result = fundService.getFundedBy(1L);

        assertThat(result.getFndFunds()).isNull();
        assertThat(result.getOtherFunds().getTokenSymbol()).isEqualTo("ZRX");
        assertThat(result.getOtherFunds().getTokenAddress()).isEqualTo(funds.get(0).getToken());
        assertThat(result.getOtherFunds().getTotalAmount()).isEqualByComparingTo("3");
    }

    @Test
    public void fundByEnrichedWithZeroes() {
        List<Fund> funds = Arrays.asList(
                FundMother.fndFundFunderNotKnown().funder("0x0").amountInWei(new BigDecimal("1000000000000000000")).build(),
                FundMother.zrxFundFunderNotKnown().funder("0x1").amountInWei(new BigDecimal("2000000000000000000")).build()
                                        );
        when(fundRepository.findByRequestId(1L)).thenReturn(funds);
        mockTokenInfo();

        FundersDto result = fundService.getFundedBy(1L);

        assertThat(result.getFunders().get(0).getOtherFunds().getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getFunders().get(1).getFndFunds().getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void mergesSameFunderData() {
        List<Fund> funds = Arrays.asList(
                FundMother.fndFundFunderNotKnown().amountInWei(new BigDecimal("1000000000000000000")).build(),
                FundMother.zrxFundFunderNotKnown().amountInWei(new BigDecimal("2000000000000000000")).build(),
                FundMother.zrxFundFunderNotKnown().amountInWei(new BigDecimal("3000000000000000000")).build(),
                FundMother.fndFundFunderNotKnown().amountInWei(new BigDecimal("2000000000000000000")).build()
                                        );
        when(fundRepository.findByRequestId(1L)).thenReturn(funds);
        mockTokenInfo();

        FundersDto result = fundService.getFundedBy(1L);

        assertThat(result.getFunders()).hasSize(1);
        assertThat(result.getFunders().get(0).getOtherFunds().getTotalAmount()).isEqualByComparingTo(new BigDecimal("5"));
        assertThat(result.getFunders().get(0).getFndFunds().getTotalAmount()).isEqualByComparingTo(new BigDecimal("3"));
    }

    private void mockTokenInfo() {
        TokenInfoDto fndTokenInfo = TokenInfoDtoMother.fnd();
        TokenInfoDto zrxTokenInfo = TokenInfoDtoMother.zrx();
        when(tokenInfoService.getTokenInfo(fndTokenInfo.getAddress())).thenReturn(fndTokenInfo);
        when(tokenInfoService.getTokenInfo(zrxTokenInfo.getAddress())).thenReturn(zrxTokenInfo);
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
        assertThat(fundArgumentCaptor.getValue().getFunderUserId()).isEqualTo(funder.getName());
    }
}