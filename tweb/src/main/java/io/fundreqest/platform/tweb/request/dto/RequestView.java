package io.fundreqest.platform.tweb.request.dto;

import io.fundrequest.core.request.fund.dto.TotalFundDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestView {
    private Long id;
    private String icon;
    private String owner;
    private String platform;
    private String repo;
    private String issueNumber;
    private String title;
    private String status;
    private Set<String> technologies;
    private TotalFundDto fndFunds;
    private TotalFundDto otherFunds;
    private Boolean starred;
}
