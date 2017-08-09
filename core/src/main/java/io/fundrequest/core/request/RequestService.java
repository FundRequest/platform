package io.fundrequest.core.request;

import io.fundrequest.core.request.command.CreateRequestCommand;
import io.fundrequest.core.request.view.RequestDto;

import java.security.Principal;
import java.util.List;

public interface RequestService {
    List<RequestDto> findAll();

    List<RequestDto> findRequestsForUser(Principal principal);

    RequestDto findRequest(Long id);

    RequestDto createRequest(Principal principal, CreateRequestCommand command);

    void addWatcherToRequest(Principal principal, Long requestId);

    void removeWatcherFromRequest(Principal principal, Long requestId);
}
