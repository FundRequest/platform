package io.fundrequest.web.request.statistics.dto;

public class StatisticsDto {
    private Long numberOfIssues;
    private Double percentageFunded;
    private Long numberOfFunders;
    private Long totalAmountFunded;
    private Double averageFundingPerIssue;

    public Long getNumberOfIssues() {
        return numberOfIssues;
    }

    public void setNumberOfIssues(Long numberOfIssues) {
        this.numberOfIssues = numberOfIssues;
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

    public Double getAverageFundingPerIssue() {
        return averageFundingPerIssue;
    }

    public void setAverageFundingPerIssue(Double averageFundingPerIssue) {
        this.averageFundingPerIssue = averageFundingPerIssue;
    }
}
