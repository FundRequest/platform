package io.fundrequest.core.request.fund;

import io.fundrequest.core.contract.service.FundRequestContractsService;
import io.fundrequest.core.infrastructure.exception.ResourceNotFoundException;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.IssueInformation;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.fiat.FiatService;
import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.request.fund.domain.PendingFund;
import io.fundrequest.core.request.fund.domain.Refund;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.dto.FundFundsByFunderAggregator;
import io.fundrequest.core.request.fund.dto.FundsAndRefundsAggregator;
import io.fundrequest.core.request.fund.dto.FundsForRequestDto;
import io.fundrequest.core.request.fund.dto.RefundFundsByFunderAggregator;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import io.fundrequest.core.request.fund.infrastructure.PendingFundRepository;
import io.fundrequest.core.request.fund.infrastructure.RefundRepository;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.mapper.TokenValueMapper;
import io.fundrequest.core.token.model.TokenValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

import static java.math.BigDecimal.ZERO;

@Service
class FundServiceImpl implements FundService {

    private final FundRepository fundRepository;
    private final RefundRepository refundRepository;
    private final PendingFundRepository pendingFundRepository;
    private final RequestRepository requestRepository;
    private final Mappers mappers;
    private final ApplicationEventPublisher eventPublisher;
    private final CacheManager cacheManager;
    private final FundRequestContractsService fundRequestContractsService;
    private final FiatService fiatService;
    private final TokenValueMapper tokenValueMapper;
    private final FundFundsByFunderAggregator fundFundsByFunderAggregator;
    private final RefundFundsByFunderAggregator refundFundsByFunderAggregator;
    private final FundsAndRefundsAggregator fundsAndRefundsAggregator;

    @Autowired
    public FundServiceImpl(final FundRepository fundRepository,
                           final RefundRepository refundRepository,
                           final PendingFundRepository pendingFundRepository,
                           final RequestRepository requestRepository,
                           final Mappers mappers,
                           final ApplicationEventPublisher eventPublisher,
                           final CacheManager cacheManager,
                           final FundRequestContractsService fundRequestContractsService,
                           final FiatService fiatService,
                           final TokenValueMapper tokenValueMapper,
                           final FundFundsByFunderAggregator fundFundsByFunderAggregator,
                           final RefundFundsByFunderAggregator refundFundsByFunderAggregator,
                           final FundsAndRefundsAggregator fundsAndRefundsAggregator) {
        this.fundRepository = fundRepository;
        this.refundRepository = refundRepository;
        this.pendingFundRepository = pendingFundRepository;
        this.requestRepository = requestRepository;
        this.mappers = mappers;
        this.eventPublisher = eventPublisher;
        this.cacheManager = cacheManager;
        this.fundRequestContractsService = fundRequestContractsService;
        this.fiatService = fiatService;
        this.tokenValueMapper = tokenValueMapper;
        this.fundFundsByFunderAggregator = fundFundsByFunderAggregator;
        this.refundFundsByFunderAggregator = refundFundsByFunderAggregator;
        this.fundsAndRefundsAggregator = fundsAndRefundsAggregator;
    }

    @Transactional(readOnly = true)
    @Override
    public List<FundDto> findAll() {
        return mappers.mapList(Fund.class, FundDto.class, fundRepository.findAll());
    }

    @Transactional(readOnly = true)
    @Override
    public List<FundDto> findAll(Iterable<Long> ids) {
        return mappers.mapList(Fund.class, FundDto.class, fundRepository.findAll(ids));
    }


    @Override
    @Transactional(readOnly = true)
    public FundDto findOne(Long id) {
        return mappers.map(Fund.class, FundDto.class, fundRepository.findOne(id).orElseThrow(ResourceNotFoundException::new));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "funds", key = "#requestId")
    public List<TokenValueDto> getTotalFundsForRequest(Long requestId) {

        final Optional<Request> one = requestRepository.findOne(requestId);
        if (one.isPresent()) {
            try {
                if (one.get().getStatus() == RequestStatus.CLAIMED) {
                    return getFromClaimRepository(one.get());
                } else {
                    return getFromFundRepository(one.get());
                }
            } catch (final Exception ex) {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }

    private List<TokenValueDto> getFromClaimRepository(final Request request) {
        final Long tokenCount = fundRequestContractsService.claimRepository()
                                                           .getTokenCount(request.getIssueInformation().getPlatform().name(),
                                                                          request.getIssueInformation().getPlatformId());

        return LongStream.range(0, tokenCount)
                         .mapToObj(x -> fundRequestContractsService.claimRepository()
                                                                   .getTokenByIndex(request.getIssueInformation().getPlatform().name(),
                                                                                    request.getIssueInformation().getPlatformId(),
                                                                                    x))
                         .filter(Optional::isPresent)
                         .map(Optional::get)
                         .map(getTotalClaimFundDto(request)).collect(Collectors.toList());
    }

    private List<TokenValueDto> getFromFundRepository(final Request request) {
        final Long fundedTokenCount = fundRequestContractsService.fundRepository()
                                                                 .getFundedTokenCount(request.getIssueInformation().getPlatform().name(),
                                                                                      request.getIssueInformation().getPlatformId());
        return LongStream.range(0, fundedTokenCount)
                         .mapToObj(x -> fundRequestContractsService.fundRepository()
                                                                   .getFundedToken(request.getIssueInformation().getPlatform().name(),
                                                                                   request.getIssueInformation().getPlatformId(),
                                                                                   x)).filter(Optional::isPresent)
                         .map(Optional::get)
                         .map(getTotalFundDto(request)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FundsForRequestDto getFundsForRequestGroupedByFunder(final Long requestId) {
        final List<UserFundsDto> userFunds = getFundsAndRefundsFor(requestId);
        enrichFundsWithZeroValues(userFunds);
        final TokenValueDto fndFunds = totalFunds(userFunds, UserFundsDto::getFndFunds, UserFundsDto::getFndRefunds);
        final TokenValueDto otherFunds = totalFunds(userFunds, UserFundsDto::getOtherFunds, UserFundsDto::getOtherRefunds);
        return FundsForRequestDto.builder()
                                 .userFunds(userFunds)
                                 .fndFunds(fndFunds)
                                 .otherFunds(otherFunds)
                                 .usdFunds(fiatService.getUsdPrice(fndFunds, otherFunds))
                                 .build();
    }

    private List<UserFundsDto> getFundsAndRefundsFor(final Long requestId) {
        final List<Fund> fundsForRequest = fundRepository.findAllByRequestId(requestId);
        final List<Refund> refundsForRequest = refundRepository.findAllByRequestId(requestId);
        return fundsAndRefundsAggregator.aggregate(Stream.concat(fundFundsByFunderAggregator.aggregate(fundsForRequest).stream(),
                                                                 refundFundsByFunderAggregator.aggregate(refundsForRequest).stream())
                                                         .collect(Collectors.toList()));
    }

    private void enrichFundsWithZeroValues(final List<UserFundsDto> userFunds) {
        TokenValueDto fndFundTemplate = null;
        TokenValueDto otherFundTemplate = null;
        TokenValueDto fndRefundTemplate = null;
        TokenValueDto otherRefundTemplate = null;
        for (final UserFundsDto userFund : userFunds) {
            fndFundTemplate = Optional.ofNullable(userFund.getFndFunds()).orElse(fndFundTemplate);
            otherFundTemplate = Optional.ofNullable(userFund.getOtherFunds()).orElse(otherFundTemplate);
            fndRefundTemplate = Optional.ofNullable(userFund.getFndRefunds()).orElse(fndRefundTemplate);
            otherRefundTemplate = Optional.ofNullable(userFund.getOtherRefunds()).orElse(otherRefundTemplate);
        }

        final TokenValueDto zeroFndFundTokenValue = buildZeroTokenValueDto(fndFundTemplate);
        final TokenValueDto zeroOtherFundTokenValue = buildZeroTokenValueDto(otherFundTemplate);
        final TokenValueDto zeroFndRefundTokenValue = buildZeroTokenValueDto(fndRefundTemplate);
        final TokenValueDto zeroOtherRefundTokenValue = buildZeroTokenValueDto(otherRefundTemplate);

        for (final UserFundsDto userFund : userFunds) {
            if (noFundsAndTemplateNotNull(userFund.getFndFunds(), fndFundTemplate)) {
                userFund.setFndFunds(zeroFndFundTokenValue);
            }
            if (noFundsAndTemplateNotNull(userFund.getOtherFunds(), otherFundTemplate)) {
                userFund.setOtherFunds(zeroOtherFundTokenValue);
            }
            if (userFund.hasRefunds()) {
                if (noFundsAndTemplateNotNull(userFund.getFndRefunds(), fndRefundTemplate)) {
                    userFund.setFndRefunds(zeroFndRefundTokenValue);
                }
                if (noFundsAndTemplateNotNull(userFund.getOtherRefunds(), otherRefundTemplate)) {
                    userFund.setOtherRefunds(zeroOtherRefundTokenValue);
                }
            }
        }
    }

    private boolean noFundsAndTemplateNotNull(final TokenValueDto funds, final TokenValueDto template) {
        return funds == null && template != null;
    }

    private TokenValueDto buildZeroTokenValueDto(final TokenValueDto tokenValueTemplate) {
        return Optional.ofNullable(tokenValueTemplate)
                       .map(template -> TokenValueDto.builder()
                                                     .tokenSymbol(template.getTokenSymbol())
                                                     .tokenAddress(template.getTokenAddress())
                                                     .totalAmount(ZERO)
                                                     .build())
                       .orElse(null);
    }

    private TokenValueDto totalFunds(final List<UserFundsDto> funds, final Function<UserFundsDto, TokenValueDto> getFundsFunction, final Function<UserFundsDto, TokenValueDto> getRefundsFunction) {
        if (funds.isEmpty()) {
            return null;
        }
        final BigDecimal totalFundsValue = sumTokenValue(funds, getFundsFunction);
        final BigDecimal totalRefundsValue = sumTokenValue(funds, getRefundsFunction);
        return funds.stream()
                    .map(getFundsFunction)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .map(fund -> TokenValueDto.builder()
                                              .tokenSymbol(fund.getTokenSymbol())
                                              .tokenAddress(fund.getTokenAddress())
                                              .totalAmount(totalFundsValue.add(totalRefundsValue))
                                              .build())
                    .orElse(null);
    }

    private BigDecimal sumTokenValue(final List<UserFundsDto> funds, final Function<UserFundsDto, TokenValueDto> getFundsFunction) {
        return funds.stream()
                    .map(getFundsFunction)
                    .filter(Objects::nonNull)
                    .map(TokenValueDto::getTotalAmount)
                    .reduce(ZERO, BigDecimal::add);
    }

    @Override
    @CacheEvict(value = "funds", key = "#requestId")
    public void clearTotalFundsCache(Long requestId) {
        // Intentionally blank
    }

    private Function<String, TokenValueDto> getTotalClaimFundDto(final Request request) {
        return tokenAddress -> {
            final BigDecimal rawBalance = new BigDecimal(fundRequestContractsService.claimRepository()
                                                                                    .getAmountByToken(request.getIssueInformation().getPlatform().name(),
                                                                                                      request.getIssueInformation().getPlatformId(),
                                                                                                      tokenAddress));
            return tokenValueMapper.map(tokenAddress, rawBalance);
        };
    }

    private Function<String, TokenValueDto> getTotalFundDto(final Request request) {
        return tokenAddress -> {
            final BigDecimal rawBalance = new BigDecimal(fundRequestContractsService.fundRepository()
                                                                                    .balance(request.getIssueInformation().getPlatform().name(),
                                                                                             request.getIssueInformation().getPlatformId(),
                                                                                             tokenAddress));
            return tokenValueMapper.map(tokenAddress, rawBalance);
        };
    }

    @Override
    @Transactional
    public void addFunds(final FundsAddedCommand command) {
        final Fund.FundBuilder fundBuilder = Fund.builder()
                                                 .tokenValue(TokenValue.builder()
                                                                       .amountInWei(command.getAmountInWei())
                                                                       .tokenAddress(command.getToken())
                                                                       .build())
                                                 .requestId(command.getRequestId())
                                                 .timestamp(command.getTimestamp())
                                                 .funderAddress(command.getFunderAddress())
                                                 .blockchainEventId(command.getBlockchainEventId());
        final Optional<PendingFund> pendingFund = pendingFundRepository.findByTransactionHash(command.getTransactionHash());
        if (pendingFund.isPresent()) {
            fundBuilder.funderUserId(pendingFund.get().getUserId());
        }
        final Fund fund = fundRepository.saveAndFlush(fundBuilder.build());
        cacheManager.getCache("funds").evict(fund.getRequestId());

        eventPublisher.publishEvent(RequestFundedEvent.builder()
                                                      .fundDto(mappers.map(Fund.class, FundDto.class, fund))
                                                      .requestId(command.getRequestId())
                                                      .timestamp(command.getTimestamp())
                                                      .build());
    }

    @Override
    public Optional<TokenValueDto> getFundsFor(final Long requestId, final String funderAddress, final String tokenAddress) {
        return requestRepository.findOne(requestId)
                                .map(request -> {
                                    try {
                                        final IssueInformation issueInformation = request.getIssueInformation();
                                        final BigInteger amountFunded = fundRequestContractsService.fundRepository()
                                                                                                   .amountFunded(issueInformation.getPlatform().name(),
                                                                                                                 issueInformation.getPlatformId(),
                                                                                                                 funderAddress,
                                                                                                                 tokenAddress)
                                                                                                   .send();
                                        return tokenValueMapper.map(tokenAddress, new BigDecimal(amountFunded));
                                    } catch (Exception e) {
                                        return null;
                                    }
                                });
    }
}
