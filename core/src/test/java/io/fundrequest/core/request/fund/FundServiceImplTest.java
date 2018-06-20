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
import io.fundrequest.core.request.fund.domain.Refund;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.dto.FundWithUserDto;
import io.fundrequest.core.request.fund.dto.FundFundsByFunderAggregator;
import io.fundrequest.core.request.fund.dto.FundsAndRefundsAggregator;
import io.fundrequest.core.request.fund.dto.FundsForRequestDto;
import io.fundrequest.core.request.fund.dto.RefundFundsByFunderAggregator;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import io.fundrequest.core.request.fund.infrastructure.PendingFundRepository;
import io.fundrequest.core.request.fund.infrastructure.RefundRepository;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.view.FundDtoMother;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.token.dto.TokenValueDtoMother;
import io.fundrequest.core.token.mapper.TokenValueMapper;
import io.fundrequest.core.token.model.TokenValue;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.same;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FundServiceImplTest {

    private FundServiceImpl fundService;
    private FundRepository fundRepository;
    private RefundRepository refundRepository;
    private RequestRepository requestRepository;
    private Mappers mappers;
    private ApplicationEventPublisher eventPublisher;
    private CacheManager cacheManager;
    private FundRequestContractsService fundRequestContractsService;
    private PendingFundRepository pendingFundRepository;
    private ProfileService profileService;
    private FiatService fiatService;
    private TokenValueMapper tokenValueMapper;
    private Principal funder;
    private FundFundsByFunderAggregator fundFundsByFunderAggregator;
    private RefundFundsByFunderAggregator refundFundsByFunderAggregator;
    private FundsAndRefundsAggregator fundsAndRefundsAggregator;

    @Before
    public void setUp() {
        fundRepository = mock(FundRepository.class);
        refundRepository = mock(RefundRepository.class);
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
        tokenValueMapper = mock(TokenValueMapper.class);
        fundFundsByFunderAggregator = mock(FundFundsByFunderAggregator.class);
        refundFundsByFunderAggregator = mock(RefundFundsByFunderAggregator.class);
        fundsAndRefundsAggregator = mock(FundsAndRefundsAggregator.class);
        fundService = new FundServiceImpl(fundRepository,
                                          refundRepository,
                                          pendingFundRepository,
                                          requestRepository,
                                          mappers,
                                          eventPublisher,
                                          cacheManager,
                                          fundRequestContractsService,
                                          fiatService,
                                          tokenValueMapper,
                                          fundFundsByFunderAggregator,
                                          refundFundsByFunderAggregator,
                                          fundsAndRefundsAggregator);
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
    public void getFundsAndRefundsAggregatedByFunder() {
        final long requestId = 45L;
        final List<Fund> funds = new ArrayList<>();
        final List<Refund> refunds = new ArrayList<>();
        final String funder1 = "hjgv";
        final String funder2 = "zdfsfdsz";
        final LocalDateTime now = LocalDateTime.now();
        final List<FundWithUserDto> fundsWithUserDto = Arrays.asList(FundWithUserDto.builder()
                                                                                    .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("30000000000000000000")).build())
                                                                                    .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("15000000000000000000")).build())
                                                                                    .timestamp(now.minusDays(2))
                                                                                    .funder(funder1)
                                                                                    .build(),
                                                                     FundWithUserDto.builder()
                                                                                    .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("40000000000000000000")).build())
                                                                                    .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("21000000000000000000")).build())
                                                                                    .funder(funder2)
                                                                                    .timestamp(now.minusDays(1))
                                                                                    .build(),
                                                                     FundWithUserDto.builder()
                                                                                    .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("5000000000000000000")).build())
                                                                                    .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("9000000000000000000")).build())
                                                                                    .funder(funder2)
                                                                                    .timestamp(now.minusDays(3))
                                                                                    .build());
        final List<FundWithUserDto> refundsWithUserDto = Arrays.asList(FundWithUserDto.builder()
                                                                                      .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-15000000000000000000")).build())
                                                                                      .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("-2000000000000000000")).build())
                                                                                      .funder(funder2)
                                                                                      .timestamp(now.minusDays(3))
                                                                                      .build(),
                                                                       FundWithUserDto.builder()
                                                                                      .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-20000000000000000000")).build())
                                                                                      .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("-12000000000000000000")).build())
                                                                                      .funder(funder1)
                                                                                      .timestamp(now.minusDays(1))
                                                                                      .build(),
                                                                       FundWithUserDto.builder()
                                                                                      .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-10000000000000000000")).build())
                                                                                      .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("-8000000000000000000")).build())
                                                                                      .funder(funder2)
                                                                                      .timestamp(now.minusDays(2))
                                                                                      .build());

        when(fundRepository.findAllByRequestId(requestId)).thenReturn(funds);
        when(refundRepository.findAllByRequestId(requestId)).thenReturn(refunds);
        when(mappers.mapList(eq(Fund.class), eq(FundWithUserDto.class), same(funds))).thenReturn(fundsWithUserDto);
        when(mappers.mapList(eq(Refund.class), eq(FundWithUserDto.class), same(refunds))).thenReturn(refundsWithUserDto);

        final FundsForRequestDto result = fundService.getFundsAndRefundsGroupedByFunder(requestId);

        assertThat(result.getFundByFunders().get(0).getFunder()).isEqualTo(funder1);
        assertThat(result.getFundByFunders().get(0).getFndFunds().getTotalAmount()).isEqualTo(new BigDecimal("30000000000000000000"));
        assertThat(result.getFundByFunders().get(0).getOtherFunds().getTotalAmount()).isEqualTo(new BigDecimal("15000000000000000000"));
        assertThat(result.getFundByFunders().get(1).getFunder()).isEqualTo(funder2);
        assertThat(result.getFundByFunders().get(1).getFndFunds().getTotalAmount()).isEqualTo(new BigDecimal("-25000000000000000000"));
        assertThat(result.getFundByFunders().get(1).getOtherFunds().getTotalAmount()).isEqualTo(new BigDecimal("-10000000000000000000"));
        assertThat(result.getFundByFunders().get(2).getFunder()).isEqualTo(funder2);
        assertThat(result.getFundByFunders().get(2).getFndFunds().getTotalAmount()).isEqualTo(new BigDecimal("45000000000000000000"));
        assertThat(result.getFundByFunders().get(2).getOtherFunds().getTotalAmount()).isEqualTo(new BigDecimal("30000000000000000000"));
        assertThat(result.getFundByFunders().get(3).getFunder()).isEqualTo(funder1);
        assertThat(result.getFundByFunders().get(3).getFndFunds().getTotalAmount()).isEqualTo(new BigDecimal("-20000000000000000000"));
        assertThat(result.getFundByFunders().get(3).getOtherFunds().getTotalAmount()).isEqualTo(new BigDecimal("-12000000000000000000"));
        assertThat(result.getFndFunds().getTokenSymbol()).isEqualTo(TokenValueDtoMother.FND().build().getTokenSymbol());
        assertThat(result.getFndFunds().getTokenAddress()).isEqualTo(TokenValueDtoMother.FND().build().getTokenAddress());
        assertThat(result.getFndFunds().getTotalAmount()).isEqualTo(new BigDecimal("30000000000000000000"));
        assertThat(result.getOtherFunds().getTokenSymbol()).isEqualTo(TokenValueDtoMother.ZRX().build().getTokenSymbol());
        assertThat(result.getOtherFunds().getTokenAddress()).isEqualTo(TokenValueDtoMother.ZRX().build().getTokenAddress());
        assertThat(result.getOtherFunds().getTotalAmount()).isEqualTo(new BigDecimal("23000000000000000000"));
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