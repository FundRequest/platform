package io.fundrequest.core.request.statistics;

import io.fundrequest.core.request.claim.event.RequestClaimedEvent;
import io.fundrequest.core.request.fiat.FiatService;
import io.fundrequest.core.request.fund.dto.TotalFundDto;
import io.fundrequest.core.request.fund.event.RequestFundedEvent;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import io.fundrequest.core.request.fund.infrastructure.TokenAmountDto;
import io.fundrequest.core.request.statistics.dto.StatisticsDto;
import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.fundrequest.core.web3j.EthUtil.fromWei;
import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;
import static org.springframework.transaction.event.TransactionPhase.AFTER_COMMIT;

@Service
class StatisticsServiceImpl implements StatisticsService {

    private FundRepository fundRepository;
    private TokenInfoService tokenInfoService;
    private FiatService fiatService;
    private CacheManager cacheManager;

    public StatisticsServiceImpl(FundRepository fundRepository,
                                 TokenInfoService tokenInfoService,
                                 FiatService fiatService, CacheManager cacheManager) {
        this.fundRepository = fundRepository;
        this.tokenInfoService = tokenInfoService;
        this.fiatService = fiatService;
        this.cacheManager = cacheManager;
    }

    @Transactional(readOnly = true)
    @Override
    @Cacheable(value = "statistics", key = "'all'")
    public StatisticsDto getStatistics() {
        double totalAvailableFunding = fundRepository.getAmountPerTokenWhereRequestHasStatusFunded()
                                                     .stream()
                                                     .mapToDouble(this::mapToUsd)
                                                     .sum();

        String mostFundedProject = getMostFunded(fundRepository.getAmountPerTokenPerProjectWhereRequestHasStatusFunded());
        String mostFundedTechnology = getMostFunded(fundRepository.getAmountPerTokenPerTechnologyWhereRequestHasStatusFunded());
        return StatisticsDto.builder()
                            .totalAvailableFunding(totalAvailableFunding)
                            .mostFundedProject(mostFundedProject)
                            .mostFundedTechnology(mostFundedTechnology)
                            .build();
    }

    private String getMostFunded(List<Object[]> objects) {
        return objects
                .stream()
                .collect(Collectors.groupingBy(o -> (String) o[0],
                                               Collectors.reducing(0.0,
                                                                   o -> mapToUsd(new TokenAmountDto((String) o[1], (BigDecimal) o[2])),
                                                                   (a, b) -> a + b)
                                              )
                        )
                .entrySet().stream().max(Comparator.comparing(Map.Entry::getValue))
                .map(Map.Entry::getKey).orElse("");
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW, readOnly = true)
    public void onFunded(RequestFundedEvent fundedEvent) {
        refreshCache();
    }

    @TransactionalEventListener(phase = AFTER_COMMIT)
    @Transactional(propagation = REQUIRES_NEW, readOnly = true)
    public void onClaimed(RequestClaimedEvent claimedEvent) {
        refreshCache();
    }

    private void refreshCache() {
        cacheManager.getCache("statistics").put("all", getStatistics());
    }

    private double mapToUsd(TokenAmountDto f) {
        TokenInfoDto tokenInfo = tokenInfoService.getTokenInfo(f.getTokenAddress());
        TotalFundDto dto = TotalFundDto.builder()
                                       .totalAmount(fromWei(f.getTotalAmount(), tokenInfo.getDecimals()))
                                       .tokenAddress(f.getTokenAddress())
                                       .tokenSymbol(tokenInfo.getSymbol())
                                       .build();
        return fiatService.getUsdPrice(dto);
    }
}
