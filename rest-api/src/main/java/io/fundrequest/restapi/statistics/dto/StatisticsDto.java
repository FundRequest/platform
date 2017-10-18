package io.fundrequest.restapi.statistics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class StatisticsDto {
    private Long numberOfRequests;
    private Long requestsFunded;
    private Long numberOfFunders;
    private BigDecimal totalAmountFunded;
    private BigDecimal averageFundingPerRequest;
    private Map<LocalDate, BigDecimal> fundsPerDay;
    private Map<LocalDate, Long> requestsAddedPerDay;

    public Long getNumberOfRequests() {
        return numberOfRequests;
    }

    public void setNumberOfRequests(Long numberOfRequests) {
        this.numberOfRequests = numberOfRequests;
    }

    public Long getRequestsFunded() {
        return requestsFunded;
    }

    public void setRequestsFunded(Long requestsFunded) {
        this.requestsFunded = requestsFunded;
    }

    public Long getNumberOfFunders() {
        return numberOfFunders;
    }

    public void setNumberOfFunders(Long numberOfFunders) {
        this.numberOfFunders = numberOfFunders;
    }

    public BigDecimal getTotalAmountFunded() {
        return totalAmountFunded;
    }

    public void setTotalAmountFunded(BigDecimal totalAmountFunded) {
        this.totalAmountFunded = totalAmountFunded;
    }

    public BigDecimal getAverageFundingPerRequest() {
        return averageFundingPerRequest;
    }

    public void setAverageFundingPerRequest(BigDecimal averageFundingPerRequest) {
        this.averageFundingPerRequest = averageFundingPerRequest;
    }

    public Map<LocalDate, BigDecimal> getFundsPerDay() {
        return fundsPerDay;
    }

    public void setFundsPerDay(Map<LocalDate, BigDecimal> fundsPerDay) {
        this.fundsPerDay = fundsPerDay;
    }

    public void setRequestsAddedPerDay(Map<LocalDate, Long> requestsAddedPerDay) {
        this.requestsAddedPerDay = requestsAddedPerDay;
    }

    public Map<LocalDate, Long> getRequestsAddedPerDay() {
        return requestsAddedPerDay;
    }
}
