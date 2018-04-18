package io.fundrequest.restapi.statistics;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.restapi.infrastructure.AbstractRestController;
import io.fundrequest.restapi.statistics.dto.StatisticsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class StatisticsController extends AbstractRestController {

    private RequestService requestService;
    private FundService fundService;

    @Autowired
    public StatisticsController(RequestService requestService, FundService fundService) {
        this.requestService = requestService;
        this.fundService = fundService;
    }

    @GetMapping(PUBLIC_PATH + "/requests/statistics")
    public StatisticsDto getStatistics() {
        StatisticsDto result = new StatisticsDto();
        List<RequestDto> allRequests = requestService.findAll();
        List<FundDto> funds = fundService.findAll();
        result.setNumberOfRequests((long) allRequests.size());
        result.setNumberOfFunders(
                funds.stream().map(FundDto::getFunder).distinct().count()
                                 );
        result.setTotalAmountFunded(
                funds.stream().map(FundDto::getAmountInWei).reduce(BigDecimal.ZERO, BigDecimal::add)
                                   );
        long numberOfRequestsFunded = funds.stream().map(FundDto::getRequestId).distinct().count();
        if (numberOfRequestsFunded > 0) {
            result.setRequestsFunded(funds.stream().map(FundDto::getRequestId).distinct().count());
            result.setAverageFundingPerRequest(result.getTotalAmountFunded().divide(new BigDecimal(numberOfRequestsFunded), BigDecimal.ROUND_HALF_UP));
        } else {
            result.setRequestsFunded(0L);
            result.setAverageFundingPerRequest(BigDecimal.ZERO);
        }
        Map<LocalDate, BigDecimal> fundsPerDay = funds.stream().collect(
                Collectors.groupingBy(f -> f.getTimestamp().toLocalDate(), Collectors.mapping(FundDto::getAmountInWei, Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))));
        result.setFundsPerDay(fundsPerDay);
        Map<LocalDate, Long> requestsAddedPerDay = funds.stream().collect(Collectors.groupingBy(f -> f.getTimestamp().toLocalDate(), Collectors.counting()));
        result.setRequestsAddedPerDay(requestsAddedPerDay);
        return result;
    }
}
