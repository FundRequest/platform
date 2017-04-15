package io.fundrequest.core.request;

import io.fundrequest.core.request.view.RequestOverviewDto;

import java.util.List;

public interface RequestService {
    List<RequestOverviewDto> findAll();

    RequestOverviewDto createRequest(String user, CreateRequestCommand command);
}
