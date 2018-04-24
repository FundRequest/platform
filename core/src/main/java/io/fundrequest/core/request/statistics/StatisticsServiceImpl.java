package io.fundrequest.core.request.statistics;

import io.fundrequest.core.request.fiat.FiatService;
import io.fundrequest.core.request.fund.dto.TotalFundDto;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import io.fundrequest.core.request.fund.infrastructure.TokenAmountDto;
import io.fundrequest.core.request.infrastructure.RequestRepository;
import io.fundrequest.core.request.statistics.dto.StatisticsDto;
import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static io.fundrequest.core.request.fund.EthUtil.fromWei;

@Service
class StatisticsServiceImpl implements StatisticsService {

    private FundRepository fundRepository;
    private RequestRepository requestRepository;
    private TokenInfoService tokenInfoService;
    private FiatService fiatService;

    public StatisticsServiceImpl(FundRepository fundRepository,
                                 RequestRepository requestRepository,
                                 TokenInfoService tokenInfoService,
                                 FiatService fiatService) {
        this.fundRepository = fundRepository;
        this.requestRepository = requestRepository;
        this.tokenInfoService = tokenInfoService;
        this.fiatService = fiatService;
    }

    @Transactional(readOnly = true)
    @Override
    public StatisticsDto getStatistics() {
        double totalAvailableFunding = fundRepository.getAmountPerTokenWhereRequestHasStatusFunded()
                                                     .stream()
                                                     .mapToDouble(this::mapToUsd)
                                                     .sum();

        String mostFundedProject = getMostFunded(fundRepository.getAmountPerTokenPerProjectWhereRequestHasStatusFunded());
        String mostFundedTechnology = getMostFunded(fundRepository.getAmountPerTokenPerTechnologyWhereRequestHasStatusFunded());
        return StatisticsDto
                .builder()
                .totalAvailableFunding(totalAvailableFunding)
                .mostFundedProject(mostFundedProject)
                .mostFundedTechnology(mostFundedTechnology)
                .build();
    }

    @NotNull
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
