package io.fundrequest.web.request.statistics;

import io.fundrequest.core.request.RequestService;
import io.fundrequest.core.request.fund.FundService;
import io.fundrequest.core.request.fund.dto.FundDto;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.web.request.statistics.dto.StatisticsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class StatisticsController {

    private RequestService requestService;
    private FundService fundService;

    @Autowired
    public StatisticsController(RequestService requestService, FundService fundService) {
        this.requestService = requestService;
        this.fundService = fundService;
    }

    @GetMapping("/requests/statistics")
    public StatisticsDto getStatistics() {
        StatisticsDto result = new StatisticsDto();
        List<RequestDto> allRequests = requestService.findAll();
        List<FundDto> funds = fundService.findAll();
        result.setNumberOfRequests((long) allRequests.size());
        result.setNumberOfFunders(
                funds.stream().map(FundDto::getFunder).distinct().count()
        );
        result.setTotalAmountFunded(
                funds.stream().mapToLong(FundDto::getAmountInWei).sum()
        );
        long numberOfRequestsFunded = funds.stream().map(FundDto::getRequestId).distinct().count();
        if (numberOfRequestsFunded > 0) {
            result.setPercentageFunded((double) numberOfRequestsFunded / (double) result.getNumberOfRequests() * 100);
            result.setAverageFundingPerRequest((double) result.getTotalAmountFunded() / (numberOfRequestsFunded));
        } else {
            result.setPercentageFunded(0d);
            result.setAverageFundingPerRequest(0d);
        }
        Map<LocalDate, Long> fundsPerDay = funds.stream().collect(Collectors.groupingBy(f -> f.getCreationDate().toLocalDate(), Collectors.summingLong(FundDto::getAmountInWei)));
        result.setFundsPerDay(fundsPerDay);
        Map<LocalDate, Long> requestsAddedPerDay = funds.stream().collect(Collectors.groupingBy(f -> f.getCreationDate().toLocalDate(), Collectors.counting()));
        result.setRequestsAddedPerDay(requestsAddedPerDay);
        return result;
    }
}
