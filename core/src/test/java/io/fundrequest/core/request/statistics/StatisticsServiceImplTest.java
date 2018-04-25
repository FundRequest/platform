package io.fundrequest.core.request.statistics;

import io.fundrequest.core.request.fiat.FiatService;
import io.fundrequest.core.request.fund.EthUtil;
import io.fundrequest.core.request.fund.dto.TotalFundDto;
import io.fundrequest.core.request.fund.infrastructure.FundRepository;
import io.fundrequest.core.request.fund.infrastructure.TokenAmountDto;
import io.fundrequest.core.request.statistics.dto.StatisticsDto;
import io.fundrequest.core.token.TokenInfoService;
import io.fundrequest.core.token.dto.TokenInfoDto;
import org.junit.Before;
import org.junit.Test;
import org.springframework.cache.CacheManager;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static io.fundrequest.core.token.dto.TokenInfoDtoMother.fnd;
import static io.fundrequest.core.token.dto.TokenInfoDtoMother.zrx;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class StatisticsServiceImplTest {

    private StatisticsServiceImpl statisticsService;
    private FundRepository fundRepository;
    private TokenInfoService tokenInfoService;
    private FiatService fiatService;
    private TokenInfoDto fnd;
    private TokenInfoDto zrx;

    @Before
    public void setUp() throws Exception {
        fundRepository = mock(FundRepository.class);
        tokenInfoService = mock(TokenInfoService.class);
        fiatService = mock(FiatService.class);
        statisticsService = new StatisticsServiceImpl(fundRepository, tokenInfoService, fiatService, mock(CacheManager.class));
        fnd = fnd();
        zrx = zrx();
        when(tokenInfoService.getTokenInfo(fnd.getAddress())).thenReturn(fnd);
        when(tokenInfoService.getTokenInfo(zrx.getAddress())).thenReturn(zrx);
    }

    @Test
    public void statisticsReturnsTotalAvailableFunding() {
        TokenAmountDto tokenAmount = new TokenAmountDto(fnd.getAddress(), EthUtil.toWei(BigDecimal.TEN, fnd.getDecimals()));
        TokenAmountDto tokenAmount2 = new TokenAmountDto(zrx.getAddress(), EthUtil.toWei(BigDecimal.ONE, zrx.getDecimals()));
        when(fundRepository.getAmountPerTokenWhereRequestHasStatusFunded()).thenReturn(Arrays.asList(tokenAmount, tokenAmount2));
        when(fiatService.getUsdPrice(TotalFundDto.builder()
                                                 .tokenSymbol(fnd.getSymbol())
                                                 .tokenAddress(fnd.getAddress())
                                                 .totalAmount(EthUtil.fromWei(tokenAmount.getTotalAmount(), fnd.getDecimals()))
                                                 .build())).thenReturn(10.0);


        when(fiatService.getUsdPrice(TotalFundDto.builder()
                                                 .tokenSymbol(zrx.getSymbol())
                                                 .tokenAddress(zrx.getAddress())
                                                 .totalAmount(EthUtil.fromWei(tokenAmount2.getTotalAmount(), zrx.getDecimals()))
                                                 .build())).thenReturn(20.0);


        StatisticsDto result = statisticsService.getStatistics();

        assertThat(result.getTotalAvailableFunding()).isEqualTo(30.0);
    }

    @Test
    public void statisticsReturnsMostFundedProject() {
        TokenAmountDto tokenAmount = new TokenAmountDto(fnd.getAddress(), EthUtil.toWei(BigDecimal.TEN, fnd.getDecimals()));
        TokenAmountDto tokenAmount2 = new TokenAmountDto(zrx.getAddress(), EthUtil.toWei(BigDecimal.ONE, zrx.getDecimals()));
        List<Object[]> requests = Arrays.asList(
                createObject("FundRequest", fnd.getAddress(), tokenAmount.getTotalAmount()),
                createObject("Dock", zrx.getAddress(), tokenAmount2.getTotalAmount())
                                               );
        when(fundRepository.getAmountPerTokenPerProjectWhereRequestHasStatusFunded()).thenReturn(requests);
        when(fiatService.getUsdPrice(TotalFundDto.builder()
                                                 .tokenSymbol(fnd.getSymbol())
                                                 .tokenAddress(fnd.getAddress())
                                                 .totalAmount(EthUtil.fromWei(tokenAmount.getTotalAmount(), fnd.getDecimals()))
                                                 .build())).thenReturn(10.0);
        when(fiatService.getUsdPrice(TotalFundDto.builder()
                                                 .tokenSymbol(zrx.getSymbol())
                                                 .tokenAddress(zrx.getAddress())
                                                 .totalAmount(EthUtil.fromWei(tokenAmount2.getTotalAmount(), zrx.getDecimals()))
                                                 .build())).thenReturn(20.0);

        StatisticsDto result = statisticsService.getStatistics();

        assertThat(result.getMostFundedProject()).isEqualTo("Dock");
    }

    @Test
    public void statisticsReturnsMostFundedTechnology() {
        TokenAmountDto tokenAmount = new TokenAmountDto(fnd.getAddress(), EthUtil.toWei(BigDecimal.TEN, fnd.getDecimals()));
        TokenAmountDto tokenAmount2 = new TokenAmountDto(zrx.getAddress(), EthUtil.toWei(BigDecimal.ONE, zrx.getDecimals()));
        List<Object[]> requests = Arrays.asList(
                createObject("Java", fnd.getAddress(), tokenAmount.getTotalAmount()),
                createObject("Python", zrx.getAddress(), tokenAmount2.getTotalAmount())
                                               );
        when(fundRepository.getAmountPerTokenPerTechnologyWhereRequestHasStatusFunded()).thenReturn(requests);
        when(fiatService.getUsdPrice(TotalFundDto.builder()
                                                 .tokenSymbol(fnd.getSymbol())
                                                 .tokenAddress(fnd.getAddress())
                                                 .totalAmount(EthUtil.fromWei(tokenAmount.getTotalAmount(), fnd.getDecimals()))
                                                 .build())).thenReturn(10.0);
        when(fiatService.getUsdPrice(TotalFundDto.builder()
                                                 .tokenSymbol(zrx.getSymbol())
                                                 .tokenAddress(zrx.getAddress())
                                                 .totalAmount(EthUtil.fromWei(tokenAmount2.getTotalAmount(), zrx.getDecimals()))
                                                 .build())).thenReturn(20.0);

        StatisticsDto result = statisticsService.getStatistics();

        assertThat(result.getMostFundedTechnology()).isEqualTo("Python");
    }

    private Object[] createObject(String techProject, String token, BigDecimal amount) {
        return new Object[] {techProject, token, amount};
    }
}