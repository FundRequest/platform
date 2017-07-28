package io.fundrequest.core.request.mapper;

import io.fundrequest.core.infrastructure.SecurityContextService;
import io.fundrequest.core.request.RequestOverviewDtoMapper;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.view.RequestOverviewDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;

public abstract class RequestOverviewDtoMapperDecorator implements RequestOverviewDtoMapper {

    @Autowired
    @Qualifier("delegate")
    private RequestOverviewDtoMapper delegate;

    @Autowired
    private SecurityContextService securityContextService;

    public RequestOverviewDto map(Request request) {
        RequestOverviewDto result = delegate.map(request);
        Authentication currentAuth = securityContextService.getLoggedInUser();
        if (result != null && currentAuth != null) {
            result.setLoggedInUserIsWatcher(request.getWatchers().contains(currentAuth.getName()));
        }
        return result;
    }
}
