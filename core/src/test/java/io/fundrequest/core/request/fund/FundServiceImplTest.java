package io.fundrequest.core.request.fund;


import io.fundrequest.core.contract.service.FundRequestContractsService;
import io.fundrequest.common.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.BlockchainEvent;
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
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.dto.TokenValueDtoMother;
import io.fundrequest.core.token.mapper.TokenValueDtoMapper;
import io.fundrequest.core.token.mapper.TokenValueMapper;
import io.fundrequest.core.token.model.TokenValue;
import io.fundrequest.core.token.model.TokenValueMother;
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
import java.util.*;
import java.util.stream.Collectors;

import static io.fundrequest.core.token.model.TokenValueMother.FND;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class FundServiceImplTest {

    private FundServiceImpl fundService;
    private FundRepository fundRepository;
    private RequestRepository requestRepository;
    private Mappers mappers;
    private ApplicationEventPublisher eventPublisher;
    private CacheManager cacheManager;
    private FundRequestContractsService fundRequestContractsService;
    private PendingFundRepository pendingFundRepository;
    private ProfileService profileService;
    private FiatService fiatService;
    private TokenValueMapper tokenValueMapper;
    private TokenValueDtoMapper tokenValueDtoMapper;
    private Principal funder;

    @Before
    public void setUp() {
        fundRepository = mock(FundRepository.class);
        pendingFundRepository = mock(PendingFundRepository.class);
        requestRepository = mock(RequestRepository.class);
        mappers = mock(Mappers.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        cacheManager = mock(CacheManager.class, RETURNS_DEEP_STUBS);
        fundRequestContractsService = mock(FundRequestContractsService.class);
        profileService = mock(ProfileService.class);
        fiatService = mock(FiatService.class);
        when(fundRepository.saveAndFlush(any(Fund.class))).then(returnsFirstArg());
        UserProfile user = UserProfileMother.davy();
        funder = user::getId;
        when(profileService.getUserProfile(funder.getName())).thenReturn(user);
        tokenValueMapper = mock(TokenValueMapper.class);tokenValueDtoMapper = mock(TokenValueDtoMapper.class);fundService = new FundServiceImpl(
                fundRepository,
                pendingFundRepository,
                requestRepository,
                mappers,
                eventPublisher,
                cacheManager,

                fundRequestContractsService,
                profileService,
                fiatService,
                tokenValueMapper,
                                          tokenValueDtoMapper);
    }

    @Test
    public void findAll() {
        List<Fund> funds = singletonList(FundMother.fndFundFunderKnown().build());
        when(fundRepository.findAll()).thenReturn(funds);
        List<FundDto> expecedFunds = singletonList(FundDtoMother.aFundDto().build());
        when(mappers.mapList(Fund.class, FundDto.class, funds)).thenReturn(expecedFunds);

        List<FundDto> result = fundService.findAll();

        assertThat(result).isEqualTo(expecedFunds);
    }

    @Test
    public void findAllByIterable() {
        List<Fund> funds = singletonList(FundMother.fndFundFunderKnown().build());
        Set<Long> ids = funds.stream().map(Fund::getId).collect(Collectors.toSet());
        when(fundRepository.findAll(ids)).thenReturn(funds);
        List<FundDto> expecedFunds = singletonList(FundDtoMother.aFundDto().build());
        when(mappers.mapList(Fund.class, FundDto.class, funds)).thenReturn(expecedFunds);

        List<FundDto> result = fundService.findAll(ids);

        assertThat(result).isEqualTo(expecedFunds);
    }

    @Test
    public void saveFunds() {
        final Request request = RequestMother.freeCodeCampNoUserStories().build();

        final FundsAddedCommand command = FundsAddedCommand.builder()
                                                           .requestId(request.getId())
                                                           .amountInWei(BigDecimal.TEN)
                                                           .blockchainEventId(465L)
                                                           .transactionHash("trans_hash")
                                                           .funderAddress("address")
                                                           .timestamp(LocalDateTime.now())
                                                           .token("token")
                                                           .build();
        final FundDto fundDto = FundDtoMother.aFundDto().build();
        final RequestDto requestDto = new RequestDto();
        final Cache cache = mock(Cache.class);
        final BlockchainEvent blockchainEvent = mock(BlockchainEvent.class);

        when(requestRepository.findOne(request.getId())).thenReturn(Optional.of(request));
        when(mappers.map(eq(Fund.class), eq(FundDto.class), any(Fund.class))).thenReturn(fundDto);
        when(mappers.map(eq(Request.class), eq(RequestDto.class), any(Request.class))).thenReturn(requestDto);
        when(cacheManager.getCache("funds")).thenReturn(cache);
        when(pendingFundRepository.findByTransactionHash(command.getTransactionHash())).thenReturn(Optional.of(PendingFund.builder().userId(funder.getName()).build()));

        fundService.addFunds(command);

        verifyFundsSaved(command, funder);
        verifyEventCreated(request.getId(), fundDto);
        verify(cache).evict(request.getId());
    }

    @Test
    public void fundedByHasNameFunder() {
        final UserProfile davy = UserProfileMother.davy();
        final Fund fndFund = FundMother.fndFundFunderKnown().tokenValue(FND().amountInWei(new BigDecimal("1000000000000000000")).build()).build();
        final List<Fund> funds = Arrays.asList(fndFund);
        final TokenValueDto tokenValueDto = TokenValueDtoMother.FND().totalAmount(new BigDecimal("1")).build();

        when(fundRepository.findByRequestId(1L)).thenReturn(funds);
        when(tokenValueDtoMapper.map(fndFund.getTokenValue())).thenReturn(tokenValueDto);
        when(fundRepository.findByRequestId(1L)).thenReturn(funds);
        when(profileService.getUserProfile(funds.get(0).getFunderUserId())).thenReturn(davy);

        final FundersDto result = fundService.getFundedBy(funder, 1L);

        assertThat(result.getFunders().get(0).getFunder()).isEqualTo(davy.getName());
        assertThat(result.getFunders().get(0).isLoggedInUser()).isTrue();
    }

    @Test
    public void fundersNoPrincipal() {
        final UserProfile davy = UserProfileMother.davy();
        final Fund fndFund = FundMother.fndFundFunderKnown().build();
        final List<Fund> funds = Collections.singletonList(fndFund);

        when(fundRepository.findByRequestId(1L)).thenReturn(funds);
        when(profileService.getUserProfile(funds.get(0).getFunderUserId())).thenReturn(davy);
        when(tokenValueDtoMapper.map(fndFund.getTokenValue())).thenReturn(TokenValueDtoMother.FND().totalAmount(new BigDecimal("1")).build());

        final FundersDto result = fundService.getFundedBy(null, 1L);

        assertThat(result.getFunders().get(0).getFunder()).isEqualTo(davy.getName());
        assertThat(result.getFunders().get(0).isLoggedInUser()).isFalse();
    }

    @Test
    public void fundedByHasNameFunderAddress() {
        final Fund fndFund = FundMother.fndFundFunderNotKnown().build();
        List<Fund> funds = Collections.singletonList(fndFund);
        final TokenValueDto tokenValueDto = TokenValueDtoMother.FND().totalAmount(new BigDecimal("1")).build();

        when(fundRepository.findByRequestId(1L)).thenReturn(funds);
        when(tokenValueDtoMapper.map(fndFund.getTokenValue())).thenReturn(tokenValueDto);

        FundersDto result = fundService.getFundedBy(funder, 1L);

        assertThat(result.getFunders().get(0).getFunder()).isEqualTo(funds.get(0).getFunder());
    }

    @Test
    public void fundedByHasTotalFnd() {
        final Fund fndFund1 = FundMother.fndFundFunderNotKnown().tokenValue(TokenValueMother.FND().amountInWei(new BigDecimal("1000000000000000000")).build()).build();
        final Fund fndFund2 = FundMother.fndFundFunderNotKnown().tokenValue(TokenValueMother.FND().amountInWei(new BigDecimal("2000000000000000000")).build()).build();
        final List<Fund> funds = Arrays.asList(fndFund1, fndFund2);
        final TokenValueDto tokenValueDto1 = TokenValueDtoMother.FND().totalAmount(new BigDecimal("1")).build();
        final TokenValueDto tokenValueDto2 = TokenValueDtoMother.FND().totalAmount(new BigDecimal("2")).build();

        when(fundRepository.findByRequestId(1L)).thenReturn(funds);
        when(tokenValueDtoMapper.map(fndFund1.getTokenValue())).thenReturn(tokenValueDto1);
        when(tokenValueDtoMapper.map(fndFund2.getTokenValue())).thenReturn(tokenValueDto2);

        FundersDto result = fundService.getFundedBy(funder, 1L);

        assertThat(result.getOtherFunds()).isNull();
        assertThat(result.getFndFunds()).isEqualTo(TokenValueDtoMother.FND().totalAmount(new BigDecimal("3")).build());
    }

    @Test
    public void fundedByHasTotalOther() {
        final Fund zrxFund1 = FundMother.zrxFundFunderNotKnown().tokenValue(TokenValueMother.ZRX().amountInWei(new BigDecimal("1000000000000000000")).build()).build();
        final Fund zrxFund2 = FundMother.zrxFundFunderNotKnown().tokenValue(TokenValueMother.ZRX().amountInWei(new BigDecimal("2000000000000000000")).build()).build();
        final List<Fund> funds = Arrays.asList(zrxFund1, zrxFund2);
        final TokenValueDto tokenValueDto1 = TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("1")).build();
        final TokenValueDto tokenValueDto2 = TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("2")).build();

        when(fundRepository.findByRequestId(1L)).thenReturn(funds);
        when(tokenValueDtoMapper.map(zrxFund1.getTokenValue())).thenReturn(tokenValueDto1);
        when(tokenValueDtoMapper.map(zrxFund2.getTokenValue())).thenReturn(tokenValueDto2);

        final FundersDto result = fundService.getFundedBy(funder, 1L);

        assertThat(result.getFndFunds()).isNull();
        assertThat(result.getOtherFunds()).isEqualTo(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("3")).build());
    }

    @Test
    public void fundByEnrichedWithZeroes() {
        final Fund fndFund = FundMother.fndFundFunderNotKnown().funder("0x0").tokenValue(TokenValueMother.FND().amountInWei(new BigDecimal("1000000000000000000")).build()).build();
        final Fund zrxFund = FundMother.zrxFundFunderNotKnown().funder("0x1").tokenValue(TokenValueMother.ZRX().amountInWei(new BigDecimal("2000000000000000000")).build()).build();
        final List<Fund> funds = Arrays.asList(fndFund, zrxFund);
        final TokenValueDto tokenValueDto1 = TokenValueDtoMother.FND().totalAmount(new BigDecimal("1")).build();
        final TokenValueDto tokenValueDto2 = TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("2")).build();

        when(fundRepository.findByRequestId(1L)).thenReturn(funds);
        when(tokenValueDtoMapper.map(fndFund.getTokenValue())).thenReturn(tokenValueDto1);
        when(tokenValueDtoMapper.map(zrxFund.getTokenValue())).thenReturn(tokenValueDto2);

        final FundersDto result = fundService.getFundedBy(funder, 1L);

        assertThat(result.getFunders().get(0).getOtherFunds().getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(result.getFunders().get(1).getFndFunds().getTotalAmount()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    public void mergesSameFunderData() {
        final Fund fund1 = FundMother.fndFundFunderNotKnown().tokenValue(TokenValueMother.FND().amountInWei(new BigDecimal("1000000000000000000")).build()).build();
        final Fund fund2 = FundMother.zrxFundFunderNotKnown().tokenValue(TokenValueMother.ZRX().amountInWei(new BigDecimal("2000000000000000000")).build()).build();
        final Fund fund3 = FundMother.zrxFundFunderNotKnown().tokenValue(TokenValueMother.ZRX().amountInWei(new BigDecimal("3000000000000000000")).build()).build();
        final Fund fund4 = FundMother.fndFundFunderNotKnown().tokenValue(TokenValueMother.FND().amountInWei(new BigDecimal("2000000000000000000")).build()).build();
        final List<Fund> funds = Arrays.asList(fund1, fund2, fund3, fund4);
        final TokenValueDto tokenValueDto1 = TokenValueDtoMother.FND().totalAmount(new BigDecimal("1")).build();
        final TokenValueDto tokenValueDto2 = TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("2")).build();
        final TokenValueDto tokenValueDto3 = TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("3")).build();
        final TokenValueDto tokenValueDto4 = TokenValueDtoMother.FND().totalAmount(new BigDecimal("2")).build();

        when(fundRepository.findByRequestId(1L)).thenReturn(funds);
        when(tokenValueDtoMapper.map(fund1.getTokenValue())).thenReturn(tokenValueDto1);
        when(tokenValueDtoMapper.map(fund2.getTokenValue())).thenReturn(tokenValueDto2);
        when(tokenValueDtoMapper.map(fund3.getTokenValue())).thenReturn(tokenValueDto3);
        when(tokenValueDtoMapper.map(fund4.getTokenValue())).thenReturn(tokenValueDto4);

        final FundersDto result = fundService.getFundedBy(funder, 1L);

        assertThat(result.getFunders()).hasSize(1);
        assertThat(result.getFunders().get(0).getOtherFunds().getTotalAmount()).isEqualByComparingTo(new BigDecimal("5"));
        assertThat(result.getFunders().get(0).getFndFunds().getTotalAmount()).isEqualByComparingTo(new BigDecimal("3"));
    }

    private void verifyEventCreated(Long requestId, FundDto fundDto) {
        final ArgumentCaptor<RequestFundedEvent> requestFundedEventArgumentCaptor = ArgumentCaptor.forClass(RequestFundedEvent.class);

        verify(eventPublisher).publishEvent(requestFundedEventArgumentCaptor.capture());

        final RequestFundedEvent event = requestFundedEventArgumentCaptor.getValue();
        assertThat(event.getFundDto()).isEqualTo(fundDto);
        assertThat(event.getRequestId()).isEqualTo(requestId);
    }

    private void verifyFundsSaved(final FundsAddedCommand command, final Principal funder) {
        ArgumentCaptor<Fund> fundArgumentCaptor = ArgumentCaptor.forClass(Fund.class);
        verify(fundRepository).saveAndFlush(fundArgumentCaptor.capture());
        assertThat(fundArgumentCaptor.getValue().getRequestId()).isEqualTo(command.getRequestId());
        assertThat(fundArgumentCaptor.getValue().getTokenValue()).isEqualTo(TokenValue.builder().amountInWei(command.getAmountInWei()).tokenAddress(command.getToken()).build());
        assertThat(fundArgumentCaptor.getValue().getFunderUserId()).isEqualTo(funder.getName());
        assertThat(fundArgumentCaptor.getValue().getBlockchainEventId()).isEqualTo(command.getBlockchainEventId());
    }
}