package io.fundrequest.core.request.fund;


import io.fundrequest.core.contract.service.FundRequestContractsService;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.FundMother;
import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestMother;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.fiat.FiatService;
import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.request.fund.domain.PendingFund;
import io.fundrequest.core.request.fund.domain.Refund;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.dto.FundFundsByFunderAggregator;
import io.fundrequest.core.request.fund.dto.FundsAndRefundsAggregator;
import io.fundrequest.core.request.fund.dto.FundsByFunderDto;
import io.fundrequest.core.request.fund.dto.FundsForRequestDto;
import io.fundrequest.core.request.fund.dto.RefundFundsByFunderAggregator;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import io.fundrequest.core.request.fund.infrastructure.PendingFundRepository;
import io.fundrequest.core.request.fund.infrastructure.RefundRepository;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.view.FundDtoMother;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.dto.TokenValueDtoMother;
import io.fundrequest.core.token.mapper.TokenValueMapper;
import io.fundrequest.core.token.model.TokenValue;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private PendingFundRepository pendingFundRepository;
    private RequestRepository requestRepository;
    private Mappers mappers;
    private ApplicationEventPublisher eventPublisher;
    private CacheManager cacheManager;
    private FundRequestContractsService fundRequestContractsService;
    private FiatService fiatService;
    private TokenValueMapper tokenValueMapper;
    private FundFundsByFunderAggregator fundFundsByFunderAggregator;
    private RefundFundsByFunderAggregator refundFundsByFunderAggregator;
    private FundsAndRefundsAggregator fundsAndRefundsAggregator;
    private Principal funder;

    @Before
    public void setUp() {
        fundRepository = mock(FundRepository.class);
        refundRepository = mock(RefundRepository.class);
        pendingFundRepository = mock(PendingFundRepository.class);
        requestRepository = mock(RequestRepository.class);
        mappers = mock(Mappers.class);
        eventPublisher = mock(ApplicationEventPublisher.class);
        cacheManager = mock(CacheManager.class, RETURNS_DEEP_STUBS);
        fundRequestContractsService = mock(FundRequestContractsService.class, RETURNS_DEEP_STUBS);
        fiatService = mock(FiatService.class);
        funder = UserProfileMother.davy()::getId;
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

        when(fundRepository.saveAndFlush(any(Fund.class))).then(returnsFirstArg());
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
    public void getFundsForRequestGroupedByFunder() {
        final long requestId = 45L;
        final List<Fund> funds = new ArrayList<>();
        final List<Refund> refunds = new ArrayList<>();
        final String funder1UserId = "46534-gjh";
        final String funder2UserId = "hgfh-676";
        final String funder1Address = "0x5346547";
        final String funder2Address = "0xabcdefg";
        final List<UserFundsDto> userFunds = Arrays.asList(UserFundsDto.builder()
                                                                       .funderAddress(funder1Address)
                                                                       .funderUserId(funder1UserId)
                                                                       .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("30000000000000000000")).build())
                                                                       .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("15000000000000000000")).build())
                                                                       .fndRefunds(null)
                                                                       .otherRefunds(null)
                                                                       .build(),
                                                           UserFundsDto.builder()
                                                                       .funderAddress(funder2Address)
                                                                       .funderUserId(funder2UserId)
                                                                       .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("40000000000000000000")).build())
                                                                       .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("21000000000000000000")).build())
                                                                       .fndRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-20000000000000000000")).build())
                                                                       .otherRefunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("-21000000000000000000")).build())
                                                                       .build(),
                                                           UserFundsDto.builder()
                                                                       .funderAddress("0xd645")
                                                                       .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("30000000000000000000")).build())
                                                                       .fndRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-15000000000000000000")).build())
                                                                       .build(),
                                                           UserFundsDto.builder()
                                                                       .funderAddress("0x645f")
                                                                       .funderUserId("dfgh-766dgfh")
                                                                       .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("50000000000000000000")).build())
                                                                       .otherRefunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("-50000000000000000000")).build())
                                                                       .build());

        final List<FundsByFunderDto> fundsByFunder = Arrays.asList(FundsByFunderDto.builder().funderUserId(funder1UserId).build(), FundsByFunderDto.builder().funderUserId(funder2UserId).build());
        final List<FundsByFunderDto> refundsByFunder = Collections.singletonList(FundsByFunderDto.builder().funderUserId(funder2UserId).build());

        when(fundRepository.findAllByRequestId(requestId)).thenReturn(funds);
        when(refundRepository.findAllByRequestId(requestId)).thenReturn(refunds);
        when(fundFundsByFunderAggregator.aggregate(same(funds))).thenReturn(fundsByFunder);
        when(refundFundsByFunderAggregator.aggregate(same(refunds))).thenReturn(refundsByFunder);
        when(fundsAndRefundsAggregator.aggregate(Stream.concat(fundsByFunder.stream(), refundsByFunder.stream()).collect(Collectors.toList()))).thenReturn(userFunds);

        final FundsForRequestDto result = fundService.getFundsForRequestGroupedByFunder(requestId);

        assertThat(result.getUserFunds()).contains(UserFundsDto.builder()
                                                               .funderAddress(funder1Address)
                                                               .funderUserId(funder1UserId)
                                                               .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("30000000000000000000")).build())
                                                               .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("15000000000000000000")).build())
                                                               .fndRefunds(null)
                                                               .otherRefunds(null)
                                                               .build(),
                                                   UserFundsDto.builder()
                                                               .funderAddress(funder2Address)
                                                               .funderUserId(funder2UserId)
                                                               .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("40000000000000000000")).build())
                                                               .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("21000000000000000000")).build())
                                                               .fndRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-20000000000000000000")).build())
                                                               .otherRefunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("-21000000000000000000")).build())
                                                               .build(),
                                                   UserFundsDto.builder()
                                                               .funderAddress("0xd645")
                                                               .fndFunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("30000000000000000000")).build())
                                                               .otherFunds(TokenValueDtoMother.ZRX().totalAmount(BigDecimal.ZERO).build())
                                                               .fndRefunds(TokenValueDtoMother.FND().totalAmount(new BigDecimal("-15000000000000000000")).build())
                                                               .otherRefunds(TokenValueDtoMother.ZRX().totalAmount(BigDecimal.ZERO).build())
                                                               .build(),
                                                   UserFundsDto.builder()
                                                               .funderAddress("0x645f")
                                                               .funderUserId("dfgh-766dgfh")
                                                               .fndFunds(TokenValueDtoMother.FND().totalAmount(BigDecimal.ZERO).build())
                                                               .otherFunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("50000000000000000000")).build())
                                                               .fndRefunds(TokenValueDtoMother.FND().totalAmount(BigDecimal.ZERO).build())
                                                               .otherRefunds(TokenValueDtoMother.ZRX().totalAmount(new BigDecimal("-50000000000000000000")).build())
                                                               .build()
                                                  );
        assertThat(result.getFndFunds().getTokenSymbol()).isEqualTo(TokenValueDtoMother.FND().build().getTokenSymbol());
        assertThat(result.getFndFunds().getTokenAddress()).isEqualTo(TokenValueDtoMother.FND().build().getTokenAddress());
        assertThat(result.getFndFunds().getTotalAmount()).isEqualTo(new BigDecimal("65000000000000000000"));
        assertThat(result.getOtherFunds().getTokenSymbol()).isEqualTo(TokenValueDtoMother.ZRX().build().getTokenSymbol());
        assertThat(result.getOtherFunds().getTokenAddress()).isEqualTo(TokenValueDtoMother.ZRX().build().getTokenAddress());
        assertThat(result.getOtherFunds().getTotalAmount()).isEqualTo(new BigDecimal("15000000000000000000"));
    }

    @Test
    public void getTotalFundsForRequest() {
        final long requestId = 6457L;
        final Request request = RequestMother.fundRequestArea51().withStatus(RequestStatus.FUNDED).build();
        final IssueInformation issueInformation = request.getIssueInformation();
        final String platform = issueInformation.getPlatform().name();
        final String platformId = issueInformation.getPlatformId();
        final String tokenAddress1 = "0x64576fg";
        final String tokenAddress2 = "0x654fh987";
        final BigDecimal fndAmount = new BigDecimal("324");
        final BigDecimal zrxAmount = new BigDecimal("762");
        final TokenValueDto fndTokenValue = TokenValueDtoMother.FND().totalAmount(fndAmount).build();
        final TokenValueDto zrxTokenValue = TokenValueDtoMother.ZRX().totalAmount(zrxAmount).build();

        when(requestRepository.findOne(requestId)).thenReturn(Optional.of(request));
        when(fundRequestContractsService.fundRepository().getFundedTokenCount(platform, platformId)).thenReturn(2L);
        when(fundRequestContractsService.fundRepository().getFundedToken(platform, platformId, 0L)).thenReturn(Optional.of(tokenAddress1));
        when(fundRequestContractsService.fundRepository().getFundedToken(platform, platformId, 1L)).thenReturn(Optional.of(tokenAddress2));
        when(fundRequestContractsService.fundRepository().balance(platform, platformId, tokenAddress1)).thenReturn(fndAmount.toBigInteger());
        when(fundRequestContractsService.fundRepository().balance(platform, platformId, tokenAddress2)).thenReturn(zrxAmount.toBigInteger());

        when(tokenValueMapper.map(tokenAddress1, fndAmount)).thenReturn(fndTokenValue);
        when(tokenValueMapper.map(tokenAddress2, zrxAmount)).thenReturn(zrxTokenValue);

        final List<TokenValueDto> result = fundService.getTotalFundsForRequest(requestId);

        assertThat(result).containsExactlyInAnyOrder(fndTokenValue, zrxTokenValue);
    }

    @Test
    public void getTotalFundsForRequest_CLAIMED() {
        final long requestId = 6457L;
        final Request request = RequestMother.fundRequestArea51().withStatus(RequestStatus.CLAIMED).build();
        final IssueInformation issueInformation = request.getIssueInformation();
        final String platform = issueInformation.getPlatform().name();
        final String platformId = issueInformation.getPlatformId();
        final String tokenAddress1 = "0x64576fg";
        final String tokenAddress2 = "0x654fh987";
        final BigDecimal fndAmount = new BigDecimal("324");
        final BigDecimal zrxAmount = new BigDecimal("762");
        final TokenValueDto fndTokenValue = TokenValueDtoMother.FND().totalAmount(fndAmount).build();
        final TokenValueDto zrxTokenValue = TokenValueDtoMother.ZRX().totalAmount(zrxAmount).build();

        when(requestRepository.findOne(requestId)).thenReturn(Optional.of(request));
        when(fundRequestContractsService.claimRepository().getTokenCount(platform, platformId)).thenReturn(2L);
        when(fundRequestContractsService.claimRepository().getTokenByIndex(platform, platformId, 0L)).thenReturn(Optional.of(tokenAddress1));
        when(fundRequestContractsService.claimRepository().getTokenByIndex(platform, platformId, 1L)).thenReturn(Optional.of(tokenAddress2));
        when(fundRequestContractsService.claimRepository().getAmountByToken(platform, platformId, tokenAddress1)).thenReturn(fndAmount.toBigInteger());
        when(fundRequestContractsService.claimRepository().getAmountByToken(platform, platformId, tokenAddress2)).thenReturn(zrxAmount.toBigInteger());
        when(tokenValueMapper.map(tokenAddress1, fndAmount)).thenReturn(fndTokenValue);
        when(tokenValueMapper.map(tokenAddress2, zrxAmount)).thenReturn(zrxTokenValue);

        final List<TokenValueDto> result = fundService.getTotalFundsForRequest(requestId);

        assertThat(result).containsExactlyInAnyOrder(fndTokenValue, zrxTokenValue);
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