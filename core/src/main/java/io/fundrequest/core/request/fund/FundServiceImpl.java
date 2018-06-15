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
import io.fundrequest.core.request.fund.dto.FundByFunderAggregate;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.dto.FundsByRequestAggregate;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import io.fundrequest.core.request.fund.infrastructure.PendingFundRepository;
import io.fundrequest.core.request.fund.infrastructure.RefundRepository;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.token.dto.TokenValueDto;
import io.fundrequest.core.token.mapper.TokenValueDtoMapper;
import io.fundrequest.core.token.mapper.TokenValueMapper;
import io.fundrequest.core.token.model.TokenValue;
import io.fundrequest.platform.profile.profile.ProfileService;
import io.fundrequest.platform.profile.profile.dto.UserProfile;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.Principal;
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

    private static final String FND_TOKEN_SYMBOL = "FND";

    private final FundRepository fundRepository;
    private final RefundRepository refundRepository;
    private final PendingFundRepository pendingFundRepository;
    private final RequestRepository requestRepository;
    private final Mappers mappers;
    private final ApplicationEventPublisher eventPublisher;
    private final CacheManager cacheManager;
    private final FundRequestContractsService fundRequestContractsService;
    private final ProfileService profileService;
    private final FiatService fiatService;
    private final TokenValueMapper tokenValueMapper;
    private final TokenValueDtoMapper tokenValueDtoMapper;

    @Autowired
    public FundServiceImpl(final FundRepository fundRepository,
                           final RefundRepository refundRepository,
                           final PendingFundRepository pendingFundRepository,
                           final RequestRepository requestRepository,
                           final Mappers mappers,
                           final ApplicationEventPublisher eventPublisher,
                           final CacheManager cacheManager,
                           final FundRequestContractsService fundRequestContractsService,
                           final ProfileService profileService,
                           final FiatService fiatService,
                           final TokenValueMapper tokenValueMapper,
                           final TokenValueDtoMapper tokenValueDtoMapper) {
        this.fundRepository = fundRepository;
        this.refundRepository = refundRepository;
        this.pendingFundRepository = pendingFundRepository;
        this.requestRepository = requestRepository;
        this.mappers = mappers;
        this.eventPublisher = eventPublisher;
        this.cacheManager = cacheManager;
        this.fundRequestContractsService = fundRequestContractsService;
        this.profileService = profileService;
        this.fiatService = fiatService;
        this.tokenValueMapper = tokenValueMapper;
        this.tokenValueDtoMapper = tokenValueDtoMapper;
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
    public FundsByRequestAggregate getFundsAggregatedByFunder(final Principal principal, final Long requestId) {
        final List<FundByFunderAggregate> fundsByFunderAggregates = groupByFunder(fundRepository.findAllByRequestId(requestId)
                                                                                                .stream()
                                                                                                .map(fund -> this.mapToFundByFunderAggregate(principal, fund))
                                                                                                .filter(Objects::nonNull)
                                                                                                .sorted(Comparator.comparing(FundByFunderAggregate::getTimestamp))
                                                                                                .collect(Collectors.toList()));

        enrichFundsWithZeroValues(fundsByFunderAggregates);
        final TokenValueDto fndFunds = totalFunds(fundsByFunderAggregates, FundByFunderAggregate::getFndFunds);
        final TokenValueDto otherFunds = totalFunds(fundsByFunderAggregates, FundByFunderAggregate::getOtherFunds);
        return FundsByRequestAggregate.builder()
                                      .fundByFunderAggregates(fundsByFunderAggregates)
                                      .fndFunds(fndFunds)
                                      .otherFunds(otherFunds)
                                      .usdFunds(fiatService.getUsdPrice(fndFunds, otherFunds))
                                      .build();
    }

    @Override
    @Transactional(readOnly = true)
    public FundsByRequestAggregate getRefundsAggregatedByRequester(final Principal principal, final Long requestId) {
        final List<FundByFunderAggregate> fundList = groupByFunder(refundRepository.findAllByRequestId(requestId)
                                                                                   .stream()
                                                                                   .map(refund -> this.mapToFundByFunderAggregate(principal, refund))
                                                                                   .filter(Objects::nonNull)
                                                                                   .sorted(Comparator.comparing(FundByFunderAggregate::getTimestamp))
                                                                                   .collect(Collectors.toList()));

        enrichFundsWithZeroValues(fundList);
        final TokenValueDto fndFunds = totalFunds(fundList, FundByFunderAggregate::getFndFunds);
        final TokenValueDto otherFunds = totalFunds(fundList, FundByFunderAggregate::getOtherFunds);
        return FundsByRequestAggregate.builder()
                                      .fundByFunderAggregates(fundList)
                                      .fndFunds(fndFunds)
                                      .otherFunds(otherFunds)
                                      .usdFunds(fiatService.getUsdPrice(fndFunds, otherFunds))
                                      .build();
    }

    private List<FundByFunderAggregate> groupByFunder(final List<FundByFunderAggregate> list) {
        return list.stream()
                   .collect(Collectors.groupingBy(FundByFunderAggregate::getFunder, Collectors.reducing((a1, b1) -> {
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

    private void enrichFundsWithZeroValues(final List<FundByFunderAggregate> list) {
        TokenValueDto fndNonEmpty = null;
        TokenValueDto otherNonEmpty = null;
        for (FundByFunderAggregate f : list) {
            if (f.getFndFunds() != null) {
                fndNonEmpty = f.getFndFunds();
            }
            if (f.getOtherFunds() != null) {
                otherNonEmpty = f.getOtherFunds();
            }
        }

        for (FundByFunderAggregate f : list) {
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

    private TokenValueDto totalFunds(final List<FundByFunderAggregate> funds, final Function<FundByFunderAggregate, TokenValueDto> getFundsFunction) {
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

    private FundByFunderAggregate mapToFundByFunderAggregate(final Principal principal, final Fund fund) {
        final UserProfile loggedInUserProfile = principal == null ? null : profileService.getUserProfile(principal.getName());
        final TokenValueDto tokenValueDto = tokenValueDtoMapper.map(fund.getTokenValue());
        final String funderNameOrAddress = StringUtils.isNotBlank(fund.getFunderUserId())
                                           ? profileService.getUserProfile(fund.getFunderUserId()).getName()
                                           : fund.getFunderAddress();
        final boolean isFundedByLoggedInUser = isFundedByLoggedInUser(loggedInUserProfile, fund.getFunderUserId(), fund.getFunderAddress());
        return tokenValueDto == null ? null : FundByFunderAggregate.builder()
                                                                   .funder(funderNameOrAddress)
                                                                   .funderAddress(fund.getFunderAddress())
                                                                   .fndFunds(getFndFunds(tokenValueDto))
                                                                   .otherFunds(getOtherFunds(tokenValueDto))
                                                                   .isLoggedInUser(isFundedByLoggedInUser)
                                                                   .timestamp(fund.getTimestamp())
                                                                   .build();
    }

    private FundByFunderAggregate mapToFundByFunderAggregate(final Principal principal, final Refund refund) {
        final UserProfile loggedInUserProfile = principal == null ? null : profileService.getUserProfile(principal.getName());
        final TokenValueDto tokenValueDto = negate(tokenValueDtoMapper.map(refund.getTokenValue()));
        final String refunderNameOrAddress = StringUtils.isNotBlank(refund.getRequestedBy())
                                             ? profileService.getUserProfile(refund.getRequestedBy()).getName()
                                             : refund.getFunderAddress();
        final boolean isFundedByLoggedInUser = isFundedByLoggedInUser(loggedInUserProfile, refund.getRequestedBy(), refund.getFunderAddress());
        return tokenValueDto == null ? null : FundByFunderAggregate.builder()
                                                                   .funder(refunderNameOrAddress)
                                                                   .funderAddress(refund.getFunderAddress())
                                                                   .fndFunds(getFndFunds(tokenValueDto))
                                                                   .otherFunds(getOtherFunds(tokenValueDto))
                                                                   .isLoggedInUser(isFundedByLoggedInUser)
                                                                   .timestamp(refund.getCreationDate())
                                                                   .build();
    }

    private TokenValueDto negate(final TokenValueDto tokenValueDto) {
        if (tokenValueDto != null && tokenValueDto.getTotalAmount() != null) {
            final BigDecimal negatedTotalAmount = tokenValueDto.getTotalAmount().negate();
            tokenValueDto.setTotalAmount(negatedTotalAmount);
            return tokenValueDto;
        }
        return tokenValueDto;
    }

    private boolean isFundedByLoggedInUser(final UserProfile loggedInUserProfile, final String funderUserId, final String funderAddress) {
        return loggedInUserProfile != null && (loggedInUserProfile.getId().equals(funderUserId) || funderAddress.equalsIgnoreCase(loggedInUserProfile.getEtherAddress()));
    }

    private TokenValueDto getFndFunds(final TokenValueDto tokenValueDto) {
        return hasFNDTokenSymbol(tokenValueDto) ? tokenValueDto : null;
    }

    private TokenValueDto getOtherFunds(final TokenValueDto tokenValueDto) {
        return !hasFNDTokenSymbol(tokenValueDto) ? tokenValueDto : null;
    }

    private boolean hasFNDTokenSymbol(TokenValueDto tokenValueDto) {
        return FND_TOKEN_SYMBOL.equalsIgnoreCase(tokenValueDto.getTokenSymbol());
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
