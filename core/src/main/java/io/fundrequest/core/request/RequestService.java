package io.fundrequest.core.request;

import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.request.view.RequestOverviewDto;

import java.security.Principal;
import java.util.List;

public interface RequestService {
    List<RequestOverviewDto> findAll();

    List<RequestOverviewDto> findRequestsForUser(Principal principal);

    RequestDto findRequest(Long id);

    RequestOverviewDto createRequest(Principal principal, CreateRequestCommand command);

    void addWatcherToRequest(Principal principal, Long requestId);

    void removeWatcherFromRequest(Principal principal, Long requestId);
}
