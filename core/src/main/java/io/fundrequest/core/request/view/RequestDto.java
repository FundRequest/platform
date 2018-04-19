package io.fundrequest.core.request.view;

import io.fundrequest.core.request.domain.RequestStatus;
import io.fundrequest.core.request.domain.RequestType;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RequestDto {

    private Long id;

    private RequestStatus status;

    private RequestType type;

    private IssueInformationDto issueInformation;

    private Set<String> technologies = new HashSet<>();

    private boolean loggedInUserIsWatcher;

    private Set<String> watchers = new HashSet<>();

    private AllFundsDto funds = new AllFundsDto();

}
