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
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.fund.dto.FunderDto;
import io.fundrequest.core.request.fund.dto.FundersDto;
import io.fundrequest.core.request.fund.dto.TotalFundDto;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import io.fundrequest.core.request.fund.infrastructure.PendingFundRepository;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import io.fundrequest.platform.profile.profile.ProfileService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
class FundServiceImpl implements FundService {

    private FundRepository fundRepository;
    private PendingFundRepository pendingFundRepository;
    private RequestRepository requestRepository;
    private Mappers mappers;
    private ApplicationEventPublisher eventPublisher;
    private CacheManager cacheManager;
    private TokenInfoService tokenInfoService;
    private FundRequestContractsService fundRequestContractsService;
    private ProfileService profileService;
    private FiatService fiatService;

    @Autowired
    public FundServiceImpl(FundRepository fundRepository,
                           PendingFundRepository pendingFundRepository, RequestRepository requestRepository,
                           Mappers mappers,
                           ApplicationEventPublisher eventPublisher,
                           CacheManager cacheManager,
                           TokenInfoService tokenInfoService,
                           FundRequestContractsService fundRequestContractsService, ProfileService profileService, FiatService fiatService) {
        this.fundRepository = fundRepository;
        this.pendingFundRepository = pendingFundRepository;
        this.requestRepository = requestRepository;
        this.mappers = mappers;
        this.eventPublisher = eventPublisher;
        this.cacheManager = cacheManager;
        this.tokenInfoService = tokenInfoService;
        this.fundRequestContractsService = fundRequestContractsService;
        this.profileService = profileService;
        this.fiatService = fiatService;
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
    public List<TotalFundDto> getTotalFundsForRequest(Long requestId) {

        final Optional<Request> one = requestRepository.findOne(requestId);
        if (one.isPresent()) {
            try {
                final Request request = one.get();
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
            } catch (final Exception ex) {
                return Collections.emptyList();
            }
        } else {
            return Collections.emptyList();
        }
    }


    @Override
    @Transactional
    public void removePendingFund(String transactionHash) {
        pendingFundRepository.findByTransactionHash(transactionHash).ifPresent(pf -> pendingFundRepository.delete(pf));
    }

    @Override
    @Transactional(readOnly = true)
    public FundersDto getFundedBy(Long requestId) {
        List<FunderDto> list = fundRepository.findByRequestId(requestId)
                                             .stream()
                                             .map(this::mapToFunderDto)
                                             .filter(Objects::nonNull)
                                             .collect(Collectors.toList());
        enrichFundsWithZeroValues(list);
        TotalFundDto fndFunds = totalFndFunds(list);
        TotalFundDto otherFunds = totalOtherFunds(list);
        return FundersDto.builder()
                         .funders(list)
                         .fndFunds(fndFunds)
                         .otherFunds(otherFunds)
                         .usdFunds(fiatService.getUsdPrice(fndFunds, otherFunds))
                         .build();
    }

    private void enrichFundsWithZeroValues(List<FunderDto> list) {
        TotalFundDto fndNonEmpty = null;
        TotalFundDto otherNonEmpty = null;
        for (FunderDto f : list) {
            if (f.getFndFunds() != null) {
                fndNonEmpty = f.getFndFunds();
            }
            if (f.getOtherFunds() != null) {
                otherNonEmpty = f.getOtherFunds();
            }
        }

        for (FunderDto f : list) {
            if (fndNonEmpty != null && f.getFndFunds() == null) {
                f.setFndFunds(TotalFundDto.builder().tokenSymbol(fndNonEmpty.getTokenSymbol()).tokenAddress(fndNonEmpty.getTokenAddress()).totalAmount(BigDecimal.ZERO).build());
            }
            if (otherNonEmpty != null && f.getOtherFunds() == null) {
                f.setOtherFunds(TotalFundDto.builder()
                                            .tokenSymbol(otherNonEmpty.getTokenSymbol())
                                            .tokenAddress(otherNonEmpty.getTokenAddress())
                                            .totalAmount(BigDecimal.ZERO)
                                            .build());
            }
        }
    }

    private TotalFundDto totalFndFunds(List<FunderDto> funds) {
        if (funds.size() == 0) {
            return null;
        }
        BigDecimal totalValue = funds.stream()
                                     .filter(f -> f.getFndFunds() != null)
                                     .map(f -> f.getFndFunds().getTotalAmount())
                                     .reduce(BigDecimal.ZERO, BigDecimal::add);
        return funds
                .stream()
                .filter(f -> f.getFndFunds() != null)
                .findFirst()
                .map(f -> TotalFundDto.builder()
                                      .tokenSymbol(f.getFndFunds().getTokenSymbol())
                                      .tokenAddress(f.getFndFunds().getTokenAddress())
                                      .totalAmount(totalValue)
                                      .build())
                .orElse(null);
    }

    private TotalFundDto totalOtherFunds(List<FunderDto> funds) {
        if (funds.size() == 0) {
            return null;
        }
        BigDecimal totalValue = funds.stream()
                                     .filter(f -> f.getOtherFunds() != null)
                                     .map(f -> f.getOtherFunds().getTotalAmount())
                                     .reduce(BigDecimal.ZERO, BigDecimal::add);
        return funds
                .stream()
                .filter(f -> f.getOtherFunds() != null)
                .findFirst()
                .map(f -> TotalFundDto.builder()
                                      .tokenSymbol(f.getOtherFunds().getTokenSymbol())
                                      .tokenAddress(f.getOtherFunds().getTokenAddress())
                                      .totalAmount(totalValue)
                                      .build())
                .orElse(null);
    }

    private FunderDto mapToFunderDto(Fund f) {
        TotalFundDto totalFundDto = createTotalFund(f.getToken(), f.getAmountInWei());
        return totalFundDto == null
               ? null
               : FunderDto.builder()
                          .funder(StringUtils.isNotBlank(f.getCreatedBy()) ? profileService.getUserProfile(f.getCreatedBy()).getName() : f.getFunder())
                          .fndFunds("FND".equalsIgnoreCase(totalFundDto.getTokenSymbol()) ? totalFundDto : null)
                          .otherFunds(!"FND".equalsIgnoreCase(totalFundDto.getTokenSymbol()) ? totalFundDto : null)
                          .build();
    }

    @Override
    @CacheEvict(value = "funds", key = "#requestId")
    public void clearTotalFundsCache(Long requestId) {
    }

    private Function<String, TotalFundDto> getTotalFundDto(final Request request) {
        return tokenAddress -> {
            final BigDecimal rawBalance = new BigDecimal(fundRequestContractsService.fundRepository()
                                                                                    .balance(request.getIssueInformation().getPlatform().name(),
                                                                                             request.getIssueInformation().getPlatformId(),
                                                                                             tokenAddress));
            return createTotalFund(tokenAddress, rawBalance);
        };
    }

    private TotalFundDto createTotalFund(String tokenAddress, BigDecimal rawBalance) {
        final TokenInfoDto tokenInfo = tokenInfoService.getTokenInfo(tokenAddress);
        return tokenInfo == null
               ? null
               : TotalFundDto.builder()
                             .tokenAddress(tokenInfo.getAddress())
                             .tokenSymbol(tokenInfo.getSymbol())
                             .totalAmount(fromWei(tokenInfo, rawBalance))
                             .build();
    }

    private BigDecimal fromWei(TokenInfoDto tokenInfo, BigDecimal rawBalance) {
        final BigDecimal divider = BigDecimal.valueOf(10).pow(tokenInfo.getDecimals());
        return rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN);
    }

    @Transactional
    @Override
    public void addFunds(FundsAddedCommand command) {
        Request request = requestRepository.findOne(command.getRequestId())
                                           .orElseThrow(() -> new RuntimeException("Unable to find request"));
        Fund fund = Fund.builder()
                        .amountInWei(command.getAmountInWei())
                        .requestId(command.getRequestId())
                        .token(command.getToken())
                        .timestamp(command.getTimestamp())
                        .funder(command.getFunderAddress())
                        .build();
        Optional<PendingFund> pendingFund = pendingFundRepository.findByTransactionHash(command.getTransactionId());
        if (pendingFund.isPresent()) {
            fund.setCreatedBy(pendingFund.get().getUserId());
        }
        fund = fundRepository.saveAndFlush(fund);
        cacheManager.getCache("funds").evict(fund.getRequestId());
        if (request.getStatus() == RequestStatus.OPEN) {
            request.setStatus(RequestStatus.FUNDED);
            request = requestRepository.saveAndFlush(request);
        }
        eventPublisher.publishEvent(
                new RequestFundedEvent(
                        command.getTransactionId(), mappers.map(Fund.class, FundDto.class, fund),
                        mappers.map(Request.class, RequestDto.class, request),
                        command.getTimestamp())
                                   );

    }

}
