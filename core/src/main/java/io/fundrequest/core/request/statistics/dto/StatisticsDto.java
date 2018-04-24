package io.fundrequest.core.request.statistics.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticsDto {
    private final Double totalAvailableFunding;
    private final String mostFundedProject;
    private final String mostFundedTechnology;
}
