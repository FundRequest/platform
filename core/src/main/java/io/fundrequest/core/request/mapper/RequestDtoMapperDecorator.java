package io.fundrequest.core.request.mapper;

import io.fundrequest.core.infrastructure.SecurityContextService;
import io.fundrequest.core.request.RequestDtoMapper;
import io.fundrequest.core.request.domain.Request;
import io.fundrequest.core.request.view.RequestDto;
import io.fundrequest.core.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;

public abstract class RequestDtoMapperDecorator implements RequestDtoMapper {

    @Autowired
    @Qualifier("delegate")
    private RequestDtoMapper delegate;

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityContextService securityContextService;

    public RequestDto map(Request request) {
        RequestDto result = delegate.map(request);
        Authentication currentAuth = securityContextService.getLoggedInUser();
        if (result != null && currentAuth != null) {
            result.setLoggedInUserIsWatcher(request.getWatchers().contains(currentAuth.getName()));
        }
        result.setUserService(userService);

        return result;
    }
}
