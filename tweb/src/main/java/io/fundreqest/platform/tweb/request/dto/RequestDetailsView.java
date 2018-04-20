package io.fundreqest.platform.tweb.request.dto;

import io.fundrequest.core.request.view.AllFundsDto;
import lombok.Data;

import java.util.Set;

@Data
public class RequestDetailsView {
    private Long id;
    private String icon;
    private String owner;
    private String platform;
    private String repo;
    private String issueNumber;
    private String title;
    private String description;
    private String status;
    private Set<String> technologies;
    private AllFundsDto funds;
    private Boolean starred;
}
