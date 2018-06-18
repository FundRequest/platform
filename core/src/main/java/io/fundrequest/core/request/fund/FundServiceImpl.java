package io.fundrequest.core.request.fund;

import io.fundrequest.core.contract.service.FundRequestContractsService;
import io.fundrequest.core.infrastructure.exception.ResourceNotFoundException;
import io.fundrequest.core.infrastructure.mapping.Mappers;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.fiat.FiatService;
import io.fundrequest.core.request.fund.command.FundsAddedCommand;
import io.fundrequest.core.request.fund.domain.Fund;
import io.fundrequest.core.request.fund.domain.PendingFund;
import io.fundrequest.core.request.fund.domain.Refund;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.dto.FundWithUserDto;
import io.fundrequest.core.request.fund.dto.FundsForRequestDto;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

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
                           final TokenValueMapper tokenValueMapper) {
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
    public FundsForRequestDto getFundsAndRefundsGroupedByFunder(final Long requestId) {
        final List<FundWithUserDto> fundsAggregatedByFunder = getFundsAndRefunds(requestId);
        enrichFundsWithZeroValues(fundsAggregatedByFunder);
        final TokenValueDto fndFunds = totalFunds(fundsAggregatedByFunder, FundWithUserDto::getFndFunds);
        final TokenValueDto otherFunds = totalFunds(fundsAggregatedByFunder, FundWithUserDto::getOtherFunds);
        return FundsForRequestDto.builder()
                                 .fundByFunders(fundsAggregatedByFunder.stream()
                                                                       .sorted(Comparator.comparing(FundWithUserDto::getTimestamp)
                                                                                         .thenComparing(FundWithUserDto::getFndFunds, Comparator.comparing(TokenValueDto::getTotalAmount).reversed())
                                                                                         .thenComparing(FundWithUserDto::getOtherFunds, Comparator.comparing(TokenValueDto::getTotalAmount).reversed()))
                                                                       .collect(Collectors.toList()))
                                 .fndFunds(fndFunds)
                                 .otherFunds(otherFunds)
                                 .usdFunds(fiatService.getUsdPrice(fndFunds, otherFunds))
                                 .build();
    }

    private List<FundWithUserDto> getFundsAndRefunds(Long requestId) {
        final List<FundWithUserDto> funds = groupByFunder(mappers.mapList(Fund.class, FundWithUserDto.class, fundRepository.findAllByRequestId(requestId)));
        final List<FundWithUserDto> refunds = groupByFunder(mappers.mapList(Refund.class, FundWithUserDto.class, refundRepository.findAllByRequestId(requestId)));
        final List<FundWithUserDto> fundsAndRefunds = new ArrayList<>();
        fundsAndRefunds.addAll(funds);
        fundsAndRefunds.addAll(refunds);
        return fundsAndRefunds.stream()
                              .filter(Objects::nonNull)
                              .collect(Collectors.toList());
    }

    private TokenValueDto mergeFunds(TokenValueDto bFunds, TokenValueDto aFunds) {
        if (bFunds != null) {
            if (aFunds == null) {
                return bFunds;
            } else {
                aFunds.setTotalAmount(aFunds.getTotalAmount().add(bFunds.getTotalAmount()));
            }
        }

        return aFunds;
    }

    private List<FundWithUserDto> groupByFunder(final List<FundWithUserDto> list) {
        return list.stream()
                   .collect(Collectors.groupingBy(FundWithUserDto::getFunder, Collectors.reducing((a1, b1) -> {
                       if (a1 == null && b1 == null) {
                           return null;
                       } else if (a1 == null) {
                           return b1;
                       } else if (b1 == null) {
                           return a1;
                       }
                       a1.setFndFunds(mergeFunds(a1.getFndFunds(), b1.getFndFunds()));
                       a1.setOtherFunds(mergeFunds(a1.getOtherFunds(), b1.getOtherFunds()));
                       a1.setTimestamp(a1.getTimestamp().isAfter(b1.getTimestamp()) ? a1.getTimestamp() : b1.getTimestamp());
                       return a1;
                   })))
                   .values()
                   .stream()
                   .filter(Optional::isPresent)
                   .map(Optional::get)
                   .collect(Collectors.toList());
    }

    private void enrichFundsWithZeroValues(final List<FundWithUserDto> list) {
        TokenValueDto fndNonEmpty = null;
        TokenValueDto otherNonEmpty = null;
        for (FundWithUserDto f : list) {
            if (f.getFndFunds() != null) {
                fndNonEmpty = f.getFndFunds();
            }
            if (f.getOtherFunds() != null) {
                otherNonEmpty = f.getOtherFunds();
            }
        }

        for (FundWithUserDto f : list) {
            if (fndNonEmpty != null && f.getFndFunds() == null) {
                f.setFndFunds(TokenValueDto.builder().tokenSymbol(fndNonEmpty.getTokenSymbol()).tokenAddress(fndNonEmpty.getTokenAddress()).totalAmount(BigDecimal.ZERO).build());
            }
            if (otherNonEmpty != null && f.getOtherFunds() == null) {
                f.setOtherFunds(TokenValueDto.builder()
                                             .tokenSymbol(otherNonEmpty.getTokenSymbol())
                                             .tokenAddress(otherNonEmpty.getTokenAddress())
                                             .totalAmount(BigDecimal.ZERO)
                                             .build());
            }
        }
    }

    private TokenValueDto totalFunds(final List<FundWithUserDto> funds, final Function<FundWithUserDto, TokenValueDto> getFundsFunction) {
        if (funds.isEmpty()) {
            return null;
        }
        BigDecimal totalValue = funds.stream()
                                     .map(getFundsFunction)
                                     .filter(Objects::nonNull)
                                     .map(TokenValueDto::getTotalAmount)
                                     .reduce(BigDecimal.ZERO, BigDecimal::add);
        return funds.stream()
                    .map(getFundsFunction)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .map(f -> TokenValueDto.builder()
                                           .tokenSymbol(f.getTokenSymbol())
                                           .tokenAddress(f.getTokenAddress())
                                           .totalAmount(totalValue)
                                           .build())
                    .orElse(null);
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
}
