package io.fundrequest.web.request.statistics.dto;

import java.time.LocalDate;
import java.util.Map;

public class StatisticsDto {
    private Long numberOfRequests;
    private Double percentageFunded;
    private Long numberOfFunders;
    private Long totalAmountFunded;
    private Double averageFundingPerRequest;
    private Map<LocalDate, Long> fundsPerDay;
    private Map<LocalDate, Long> requestsAddedPerDay;

    public Long getNumberOfRequests() {
        return numberOfRequests;
    }

    public void setNumberOfRequests(Long numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
    }

    public Double getPercentageFunded() {
        return percentageFunded;
    }

    public void setPercentageFunded(Double percentageFunded) {
        this.percentageFunded = percentageFunded;
    }

    public Long getNumberOfFunders() {
        return numberOfFunders;
    }

    public void setNumberOfFunders(Long numberOfFunders) {
        this.numberOfFunders = numberOfFunders;
    }

    public Long getTotalAmountFunded() {
        return totalAmountFunded;
    }

    public void setTotalAmountFunded(Long totalAmountFunded) {
        this.totalAmountFunded = totalAmountFunded;
    }

    public Double getAverageFundingPerRequest() {
        return averageFundingPerRequest;
    }

    public void setAverageFundingPerRequest(Double averageFundingPerRequest) {
        this.averageFundingPerRequest = averageFundingPerRequest;
    }

    public Map<LocalDate, Long> getFundsPerDay() {
        return fundsPerDay;
    }

    public void setFundsPerDay(Map<LocalDate, Long> fundsPerDay) {
        this.fundsPerDay = fundsPerDay;
    }

    public void setRequestsAddedPerDay(Map<LocalDate, Long> requestsAddedPerDay) {
        this.requestsAddedPerDay = requestsAddedPerDay;
    }

    public Map<LocalDate, Long> getRequestsAddedPerDay() {
        return requestsAddedPerDay;
    }
}
